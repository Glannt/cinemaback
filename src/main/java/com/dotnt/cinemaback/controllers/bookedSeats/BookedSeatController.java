package com.dotnt.cinemaback.controllers.bookedSeats;

import com.dotnt.cinemaback.dto.response.ApiResponse;
import com.dotnt.cinemaback.models.BookedSeat;
import com.dotnt.cinemaback.services.bookedSeat.IBookedSeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/booked-seats")
@RequiredArgsConstructor
public class BookedSeatController {
     private final IBookedSeatService bookedSeatService;
     @GetMapping("/{showtimeId}")
     public ResponseEntity<ApiResponse<List<BookedSeat>>> getBookedSeatsByHallId(@PathVariable("showtimeId") UUID showtimeId) {
         List<BookedSeat> bookedSeats = bookedSeatService.getBookedSeatsByShowtimeId(showtimeId);
         return ResponseEntity.ok(ApiResponse
                 .<List<BookedSeat>>builder()
                 .code(HttpStatus.OK.value())
                 .message("Booked seats retrieved successfully")
                 .data(bookedSeats)
                 .build());
     }
}
