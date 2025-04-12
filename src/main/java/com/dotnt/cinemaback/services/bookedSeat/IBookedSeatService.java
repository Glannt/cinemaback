package com.dotnt.cinemaback.services.bookedSeat;

import com.dotnt.cinemaback.models.BookedSeat;

import java.util.List;
import java.util.UUID;

public interface IBookedSeatService {
    List<BookedSeat> getBookedSeatsByShowtimeId(UUID hallId);
}
