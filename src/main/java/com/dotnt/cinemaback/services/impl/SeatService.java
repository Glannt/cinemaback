package com.dotnt.cinemaback.services.impl;


import com.dotnt.cinemaback.constants.enums.ESeatType;
import com.dotnt.cinemaback.dto.SeatDTO;
import com.dotnt.cinemaback.exception.AppException;
import com.dotnt.cinemaback.exception.ErrorCode;
import com.dotnt.cinemaback.mapper.SeatMapper;
import com.dotnt.cinemaback.models.Hall;
import com.dotnt.cinemaback.models.HallHasSeat;
import com.dotnt.cinemaback.models.Seat;
import com.dotnt.cinemaback.models.SeatType;
import com.dotnt.cinemaback.repositories.HallRepository;
import com.dotnt.cinemaback.repositories.SeatRepository;
import com.dotnt.cinemaback.repositories.SeatTypeRepository;
import com.dotnt.cinemaback.services.ISeatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "SEAT-SERVICE")
public class SeatService implements ISeatService {
    private final SeatRepository seatRepository;
    private final HallRepository hallRepository;
    private final SeatTypeRepository seatTypeRepository;
    private final SeatMapper seatMapper;

    @Override
    public List<SeatDTO> getSeatsByHall(UUID hallId) {
        List<Seat> seats = new ArrayList<>();
        Hall hall = hallRepository.findById(hallId).orElseThrow(
                () -> new AppException(ErrorCode.ID_NOT_FOUND)
        );
        Set<HallHasSeat> hallHasSeats = hall.getHallHasSeats();
        List<SeatDTO> seatDTOs = hallHasSeats.stream()
                .map(hallSeat -> seatMapper.toDTO(hallSeat.getSeat()))
                .collect(Collectors.toList());
        return seatDTOs;
    }


    @Override
    public SeatDTO getSeatById(UUID seatId) {
        SeatDTO seatDTO = seatRepository.findById(seatId)
                .map(seatMapper::toDTO)
                .orElseThrow(() -> new AppException(ErrorCode.ID_NOT_FOUND));
        return seatDTO;
    }

    @Override
    public SeatDTO updateSeat(UUID seatId, SeatDTO seat) {
        Seat seatEntity = seatRepository.findById(seatId)
                .orElseThrow(() -> new AppException(ErrorCode.ID_NOT_FOUND));
        if (seat.getType() != null) {
            ESeatType eSeatType = ESeatType.valueOf(seat.getType());
            SeatType seatType = seatTypeRepository.findByName(eSeatType)
                    .orElseThrow(() -> new AppException(ErrorCode.SEAT_TYPE_NOT_FOUND));
            seatEntity.setSeatType(seatType);
        }

        seatRepository.save(seatEntity);
        SeatDTO dto = seatMapper.toDTO(seatEntity);
        return dto;
    }
}
