package com.dotnt.cinemaback.services.tickets;

import com.dotnt.cinemaback.constants.enums.EPaymentMethod;
import com.dotnt.cinemaback.constants.enums.ETicket;
import com.dotnt.cinemaback.constants.enums.ETransactionStatus;
import com.dotnt.cinemaback.constants.enums.SeatStatus;
import com.dotnt.cinemaback.dto.request.TicketRequestDTO;
import com.dotnt.cinemaback.dto.response.TicketResponseDTO;
import com.dotnt.cinemaback.exception.AppException;
import com.dotnt.cinemaback.exception.ErrorCode;
import com.dotnt.cinemaback.mapper.TicketMapper;
import com.dotnt.cinemaback.models.*;
import com.dotnt.cinemaback.repositories.*;
import com.dotnt.cinemaback.utils.BookingReferenceGenearator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "TICKET_SERVICE")
public class TicketService implements ITicketService {
    private final SeatRepository seatRepository;
    private final BookedSeatRepository bookedSeatRepository;
    private final TransactionRepository transactionRepository;
    private final ShowTimeRepository showtimeRepository;
    private final DiscountRepository discountRepository;
    private final TicketRepository ticketRepository;
    private BookingReferenceGenearator bookingReferenceGenearator;
    private final TicketMapper ticketMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TicketResponseDTO createTicket(TicketRequestDTO request) {
        ShowTime showtime = showtimeRepository.findById(request.getShowtimeId())
                .orElseThrow(() -> new AppException(ErrorCode.ID_NOT_FOUND));

        List<Seat> seats = seatRepository.findByRowAndNumberIn(request.getSelectedSeats());
        if (seats.stream().anyMatch(s -> s.getStatus() == SeatStatus.OCCUPIED)) {
            throw new RuntimeException("Some seats are already occupied");
        }
        seats.forEach(s -> s.setStatus(SeatStatus.OCCUPIED));

        BigDecimal totalSeatPrice = calculateTotalSeatPrice(seats);
        BigDecimal totalPrice = totalSeatPrice.add(BigDecimal.valueOf(showtime.getTicketPrice()));
        log.info("Total price: {}", totalPrice);


        Ticket ticket = Ticket
                .builder()
                .price(totalPrice)
                .bookingDate(LocalDateTime.now())
                .bookingReference(BookingReferenceGenearator.generateBookingReference())
                .type(showtimeRepository.findAll().stream().filter(s -> s.getId().equals(request.getShowtimeId())).findFirst().get().getHall().getProjectionType())
                .status(ETicket.BOOKED)
                .showtime(showtimeRepository.findAll().stream().filter(s -> s.getId().equals(request.getShowtimeId())).findFirst().get())
                .build();


        Transaction transaction = Transaction
                .builder()
                .paymentMethod(EPaymentMethod.valueOf(request.getPaymentMethod()))
                .totalPrice(totalPrice)
                .transactionTime(LocalDateTime.now())
                .status(ETransactionStatus.PENDING)
                .build();

        transactionRepository.save(transaction);

        Discount discount = discountRepository.findAll().stream().filter(d -> d.getCode().equals(request.getCouponCode())).findFirst().orElse(null);

        List<BookedSeat> bookedSeats = seats
                .stream()
                .map(s -> BookedSeat
                        .builder()
                        .seat(s)
                        .bookingTime(LocalDateTime.now())
                        .build())
                .toList();


        ticket.setBookedSeats(bookedSeats);

        ticket.setDiscount(discount);

        // Update ticket lần cuối
        ticketRepository.save(ticket);

        bookedSeats.forEach(s -> s.setTicket(ticket));

        bookedSeatRepository.saveAll(bookedSeats);
// Gắn ticket vào transaction
        transaction.setTicket(ticket);

// Save transaction
        transactionRepository.save(transaction);

        return ticketMapper.toTicketResponseDTO(ticket);


    }

    @Override
    public Page<TicketResponseDTO> getTickets(int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by("createdAt").descending());
        Page<Ticket> tickets = ticketRepository.findAll(pageable);
        return tickets.map(ticketMapper::toTicketResponseDTO);
    }

    @Override
    public TicketResponseDTO getTicketByBookingReference(String bookingReference) {
        Ticket ticket = ticketRepository.findTicketByBookingReference(bookingReference);
        return ticketMapper.toTicketResponseDTO(ticket);
    }

    @Scheduled(fixedRate = 60000) // Runs every minute
    @Transactional(rollbackFor = Exception.class)
    public void updateSeatStatus() {

        // Lấy ra các seat đang OCCUPIED và showtime của nó đã kết thúc
        List<Seat> seatsToUpdate = seatRepository.findOccupiedSeatsNative(LocalDateTime.now());
        log.info("Found {} seats to update", seatsToUpdate.size());
        int count = 0;
        log.info("NOW: {}", LocalDateTime.now());
        for (Seat seat : seatsToUpdate) {
            // Kiểm tra từng BookedSeat để đảm bảo showtime kết thúc rồi mới reset status
            boolean allShowtimesEnded = seat.getBookedSeats().stream()
                    .allMatch(bookedSeat -> bookedSeat.getTicket().getShowtime().getEndTime().isBefore(LocalDateTime.now()));

            if (allShowtimesEnded && seat.getStatus() == SeatStatus.OCCUPIED) {
                seat.setStatus(SeatStatus.AVAILABLE);
                count++;
            }
        }

        seatRepository.saveAll(seatsToUpdate);

        log.info("Updated {} seats back to AVAILABLE", count);
    }


    private BigDecimal calculateTotalSeatPrice(List<Seat> seats) {
        return seats.stream()
                .map(seat -> BigDecimal.valueOf(seat.getSeatType().getPrice()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
