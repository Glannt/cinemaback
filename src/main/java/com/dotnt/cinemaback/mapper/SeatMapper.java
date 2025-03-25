package com.dotnt.cinemaback.mapper;


import com.dotnt.cinemaback.constants.enums.ESeatType;
import com.dotnt.cinemaback.dto.SeatDTO;
import com.dotnt.cinemaback.models.Seat;
import com.dotnt.cinemaback.models.SeatType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SeatMapper {
    public SeatDTO toDTO(Seat seat) {
        return SeatDTO.builder()
                .id(seat.getId())
                .row(seat.getRow())
                .number(seat.getNumber())
                .type(String.valueOf(seat.getSeatType().getName()))
                .price(seat.getSeatType().getPrice())
                .build();
    }

    public List<SeatDTO> toDTOList(List<Seat> seats) {
        return seats.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public Seat toEntity(SeatDTO dto) {
        return Seat.builder()
                .row(dto.getRow())
                .number(dto.getNumber())
                .seatType(SeatType
                        .builder()
                        .name(ESeatType.valueOf(dto.getType()))
                        .description("Seat type")
                        .price(dto.getPrice())
                        .build())
                .build();
    }
}
