package com.dotnt.cinemaback.dto.response;

import com.dotnt.cinemaback.dto.SeatDTO;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class TicketResponseDTO {
    private String ticketId;
    private String bookingReference;
    private LocalDateTime bookingTime;
    private String paymentStatus;
    private BigDecimal totalAmount;
    private String currency;
    private LocalDateTime showtime;
    private List<SeatDTO> seats;
}
