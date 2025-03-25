package com.dotnt.cinemaback;

import com.dotnt.cinemaback.constants.enums.ETicket;
import com.dotnt.cinemaback.dto.request.TicketRequestDTO;
import com.dotnt.cinemaback.dto.response.TicketResponseDTO;
import com.dotnt.cinemaback.models.*;
import com.dotnt.cinemaback.repositories.*;
import com.dotnt.cinemaback.services.impl.TicketService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)


public class TicketTest {

    @Test
    public void testCreateTicket() {
        // Arrange
        Discount discount = mock(Discount.class);
        Customer customer = mock(Customer.class);
        ShowTime showtime = mock(ShowTime.class);
        BookedSeat bookedSeat = mock(BookedSeat.class);
        Transaction transaction = mock(Transaction.class);


        // Act
//            Ticket ticket = Ticket.builder()
//                    .bookingReference("REF123")
//                    .discount(discount)
//                    .customer(customer)
//                    .bookingDate(LocalDateTime.now())
//                    .type(ProjectionType.TWO_D)
//                    .price(BigDecimal.valueOf(10.00))
//                    .status(ETicket.BOOKED)
//                    .transactions(transaction)
//                    .showtime(showtime)
//                    .build();

        TicketService ticketService = new TicketService(
                mock(SeatRepository.class), mock(BookedSeatRepository.class), mock(TransactionRepository.class),
                mock(ShowTimeRepository.class), mock(DiscountRepository.class), mock(TicketRepository.class)

        );

        TicketRequestDTO request = TicketRequestDTO.builder()
                .paymentMethod("CREDIT_CARD")
                .showtimeId(UUID.randomUUID())
                .selectedSeats(List.of("A1", "A2"))
                .couponCode("DISCOUNT10")
                .build();
        TicketResponseDTO ticketResponse = ticketService.createTicket(request);

        assertNotNull(ticketResponse);
        assertEquals("REF123", ticketResponse.getBookingReference());
        assertEquals(BigDecimal.valueOf(10.00), ticketResponse.getTotalAmount());
        assertEquals(ETicket.BOOKED.name(), ticketResponse.getPaymentStatus());
        assertNotNull(ticketResponse.getShowtime());
        assertNotNull(ticketResponse.getSeats());

//            assertNotNull(ticket);
//            assertEquals("REF123", ticket.getBookingReference());
//            assertEquals(discount, ticket.getDiscount());
//            assertEquals(customer, ticket.getCustomer());
//            assertEquals(showtime, ticket.getShowtime());
//            assertEquals(LocalDateTime.now().getDayOfYear(), ticket.getBookingDate().getDayOfYear());
//            assertEquals(ProjectionType.TWO_D, ticket.getType());
//            assertEquals(BigDecimal.valueOf(10.00), ticket.getPrice());
//            assertEquals(ETicket.BOOKED, ticket.getStatus());
//            assertEquals(transaction, ticket.getTransactions());
    }
}
