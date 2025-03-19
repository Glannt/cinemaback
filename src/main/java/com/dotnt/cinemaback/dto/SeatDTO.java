package com.dotnt.cinemaback.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class SeatDTO {
    UUID id;
    String row;
    Integer number;
    String type;
    Double price;


}
