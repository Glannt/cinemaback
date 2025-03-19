package com.dotnt.cinemaback.dto;

import com.dotnt.cinemaback.models.Hall;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CinemaDTO {
    private UUID id;
    private String name;
    private String status;
    private String address;
    private List<Hall> halls;
}