package com.dotnt.cinemaback.controllers.halls;


import com.dotnt.cinemaback.dto.request.HallRequest;
import com.dotnt.cinemaback.dto.response.ApiResponse;
import com.dotnt.cinemaback.dto.response.HallResponse;
import com.dotnt.cinemaback.dto.response.ListResponse;
import com.dotnt.cinemaback.services.halls.IHallService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/hall")
@RequiredArgsConstructor
@Slf4j(topic = "HALL-SERVICE")
public class HallController {
    private final IHallService IHallService;

    /**
     * Tạo mới rạp chiếu phim
     *
     * @param request DTO chứa thông tin rạp cần tạo
     * @return HallResponse đã được lưu
     */
    @PostMapping
    public ApiResponse<
            HallResponse
            > createHall(@RequestBody HallRequest request, BindingResult result) {
        if (result.hasErrors()) {
            log.error("Validation error: {}", result.getAllErrors());
            return ApiResponse.<HallResponse>builder()
                    .code(HttpStatus.BAD_REQUEST.value())
                    .message("Validation error")
                    .build();
        }
        var response = IHallService.createHall(request);
        return ApiResponse.<HallResponse>builder()
                .code(HttpStatus.CREATED.value())
                .message("Hall is created")
                .data(response)
                .build();
    }

    /**
     * Cập nhật thông tin rạp chiếu phim
     *
     * @param hallId  ID của rạp cần cập nhật
     * @param request DTO chứa thông tin cần cập nhật
     * @return HallResponse đã được cập nhật
     */
    @PutMapping("{id}")
    public ApiResponse<
            HallResponse
            > updateHall(@PathVariable("id") UUID hallId, @RequestBody HallRequest request) {
        var response = IHallService.updateHall(hallId, request);
        return ApiResponse.<HallResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Hall is updated")
                .data(response)
                .build();
    }

    /**
     * Xóa rạp chiếu phim
     *
     * @param hallId ID của rạp cần xóa
     * @return thông báo xóa thành công
     */
    @GetMapping("{id}")
    public ApiResponse<HallResponse> getHall(@PathVariable("id") UUID hallId) {
        var response = IHallService.getHall(hallId);
        return ApiResponse.<HallResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Hall is fetched")
                .data(response)
                .build();
    }

    /**
     * Lấy danh sách rạp chiếu phim
     *
     * @return danh sách rạp chiếu phim
     */
    @GetMapping()
    public ResponseEntity<
            ApiResponse<
                    ListResponse<HallResponse>
                    >
            > getHalls(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int limit) {
        var halls = IHallService.getHalls(page, limit);
        ListResponse<HallResponse> response = ListResponse.<HallResponse>builder()
                .currentPage(halls.getNumber() + 1)
                .pageSize(halls.getSize())
                .totalPages(halls.getTotalPages())
                .totalElements(halls.getTotalElements())
                .isFirst(halls.isFirst())
                .isLast(halls.isLast())
                .data(halls.getContent())
                .build();
        return ResponseEntity
                .ok()
                .body(ApiResponse.<ListResponse<HallResponse>>builder()
                        .code(HttpStatus.OK.value())
                        .message("Halls are fetched")
                        .data(response)
                        .build());
    }
}
