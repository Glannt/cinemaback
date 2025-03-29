package com.dotnt.cinemaback.dto.response;


import com.dotnt.cinemaback.dto.SeatDTO;
import lombok.Builder;
import lombok.Getter;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class HallResponse {
    private UUID id;
    private String name;
    private String status;
    private List<String> seats;


}