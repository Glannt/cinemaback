package com.dotnt.cinemaback.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(pattern = "yyyy-mm-dd")
    private LocalDate showDate;
    private double price;
    private String projectionType;
}
