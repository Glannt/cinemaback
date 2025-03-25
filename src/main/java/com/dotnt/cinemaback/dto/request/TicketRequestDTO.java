package com.dotnt.cinemaback.dto.request;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class TicketRequestDTO {
    private UUID showtimeId;
    private List<String> selectedSeats;
    //    private User user;
//    private Transaction payment;
    private String paymentMethod;
    private String couponCode;
//    private List<ConcessionOrder> concessionsOrder;
}
