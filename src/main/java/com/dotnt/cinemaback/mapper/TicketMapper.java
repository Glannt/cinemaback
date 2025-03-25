package com.dotnt.cinemaback.mapper;

import com.dotnt.cinemaback.dto.SeatDTO;
import com.dotnt.cinemaback.dto.response.ShowTimeResponseDTO;
import com.dotnt.cinemaback.dto.response.TicketResponseDTO;
import com.dotnt.cinemaback.models.Ticket;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class TicketMapper {
    public static TicketResponseDTO toTicketResponseDTO(Ticket ticket) {
        return TicketResponseDTO.builder()
                .ticketId(String.valueOf(ticket.getId()))
                .bookingReference(ticket.getBookingReference())
                .totalAmount(ticket.getPrice())
                .bookingTime(ticket.getBookingDate())
                .paymentStatus(String.valueOf(ticket.getStatus()))
                .showtime(ShowTimeResponseDTO.builder()
                        .id(ticket.getShowtime().getId())
                        .showDate(ticket.getShowtime().getShowDate())
                        .movieTitle(ticket.getShowtime().getMovie().getTitle())
                        .hallName(ticket.getShowtime().getHall().getName())
                        .startTime(ticket.getShowtime().getStartTime())
                        .endTime(ticket.getShowtime().getEndTime())
                        .projectionType(String.valueOf(ticket.getShowtime().getHall().getProjectionType()))
                        .price(ticket.getShowtime().getTicketPrice())
                        .build())
                .seats(ticket.getBookedSeats().stream()
                        .map(s -> SeatDTO.builder()
                                .id(s.getSeat().getId())
                                .row(s.getSeat().getRow())
                                .number(s.getSeat().getNumber())
                                .type(String.valueOf(s.getSeat().getSeatType().getName()))
                                .price(s.getSeat().getSeatType().getPrice())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }


}
