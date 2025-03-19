package com.dotnt.cinemaback.services;

import com.dotnt.cinemaback.dto.SeatDTO;

import java.util.List;
import java.util.UUID;

public interface ISeatService {
    List<SeatDTO> getSeatsByHall(UUID hallId);

    SeatDTO getSeatById(UUID seatId);

    SeatDTO updateSeat(UUID seatId, SeatDTO seat);
}
