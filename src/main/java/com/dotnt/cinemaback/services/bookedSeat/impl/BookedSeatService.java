package com.dotnt.cinemaback.services.bookedSeat.impl;

import com.dotnt.cinemaback.models.BookedSeat;
import com.dotnt.cinemaback.repositories.BookedSeatRepository;
import com.dotnt.cinemaback.repositories.HallRepository;
import com.dotnt.cinemaback.repositories.SeatRepository;
import com.dotnt.cinemaback.services.bookedSeat.IBookedSeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookedSeatService implements IBookedSeatService {
    private final HallRepository hallRepository;
    private final BookedSeatRepository bookedSeatRepository;
    @Override
    public List<BookedSeat> getBookedSeatsByShowtimeId(UUID showTimeId) {
        return bookedSeatRepository.findAllByShowtimeId(showTimeId);
    }
}
