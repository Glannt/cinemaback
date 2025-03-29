package com.dotnt.cinemaback.mapper;

import com.dotnt.cinemaback.constants.enums.CinemaStatus;
import com.dotnt.cinemaback.dto.CinemaDTO;
import com.dotnt.cinemaback.models.Cinema;
import com.dotnt.cinemaback.models.Hall;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Component
public class CinemaMapper {
    public CinemaDTO toDto(Cinema cinema) {
        if (cinema == null) {
            return null;
        }

        return CinemaDTO.builder()
                .id(cinema.getId())
                .name(cinema.getName())
                .address(cinema.getAddress())
                .status(cinema.getStatus().toString())
                .hallIds(cinema.getHalls()
                        .stream()
                        .map(hall -> hall.getId().toString())
                        .toList())
                .build();
    }

    public Cinema toEntity(CinemaDTO cinemaDTO) {
        if (cinemaDTO == null) {
            return null;
        }

        return Cinema.builder()
                .name(cinemaDTO.getName())
                .address(cinemaDTO.getAddress())
                .status(CinemaStatus.valueOf(cinemaDTO.getStatus()))
                .build();
    }


}
