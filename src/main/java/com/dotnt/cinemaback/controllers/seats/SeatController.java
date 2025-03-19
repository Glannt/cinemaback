package com.dotnt.cinemaback.controllers.seats;

import com.dotnt.cinemaback.dto.SeatDTO;
import com.dotnt.cinemaback.dto.response.ApiResponse;
import com.dotnt.cinemaback.services.ISeatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/seat")
@RequiredArgsConstructor
@Slf4j(topic = "SEAT-CONTROLLER")
public class SeatController {
    private final ISeatService seatService;

    /**
     * Lấy danh sách ghế theo rạp
     *
     * @param hallId ID của rạp
     * @return Danh sách ghế
     */
    @GetMapping("/hall/{id}")
    public ApiResponse<List<SeatDTO>> getSeatsByHall(@PathVariable("id") UUID hallId) {
        var response = seatService.getSeatsByHall(hallId);
        return ApiResponse.<List<SeatDTO>>builder()
                .code(200)
                .message("Get seats by hall")
                .data(response)
                .build();

    }

    /**
     * Lấy thông tin ghế theo ID
     *
     * @param seatId ID của ghế
     * @return Thông tin ghế
     */
    @GetMapping("{id}")
    public ApiResponse<SeatDTO> getSeatById(@PathVariable("id") UUID seatId) {
        var response = seatService.getSeatById(seatId);
        return ApiResponse.<SeatDTO>builder()
                .code(200)
                .message("Get seat by id")
                .data(response)
                .build();
    }

    /**
     * Cập nhật thông tin ghế
     *
     * @param seatId ID của ghế
     * @param seat   Thông tin ghế cần cập nhật
     * @return Thông tin ghế sau khi cập nhật
     */
    @PutMapping("{id}")
    public ApiResponse<SeatDTO> updateSeat(@PathVariable("id") UUID seatId, @RequestBody SeatDTO seat) {
        var response = seatService.updateSeat(seatId, seat);
        return ApiResponse.<SeatDTO>builder()
                .code(200)
                .message("Update seat")
                .data(response)
                .build();
    }
}
