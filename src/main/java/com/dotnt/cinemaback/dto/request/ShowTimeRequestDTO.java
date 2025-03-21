package com.dotnt.cinemaback.dto.request;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class ShowTimeRequestDTO {
    private UUID movieId;
    private UUID hallId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDate showDate;
    private double price;
}
