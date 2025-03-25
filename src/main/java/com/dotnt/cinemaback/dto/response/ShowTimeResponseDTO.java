package com.dotnt.cinemaback.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class ShowTimeResponseDTO {
    private UUID id;
    private String movieTitle;
    private String hallName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDate showDate;
    private double price;
    private String projectionType;
}
