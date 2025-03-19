package com.dotnt.cinemaback.controllers.cinemas;

import com.dotnt.cinemaback.dto.CinemaDTO;
import com.dotnt.cinemaback.dto.response.ApiResponse;
import com.dotnt.cinemaback.services.ICinemaService;
import com.dotnt.cinemaback.validators.ValidCinemaDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j(topic = "CINEMA-CONTROLLER")
@RequestMapping("api/cinema")
public class CinemaController {
    private final ICinemaService ICinemaService;

    @PostMapping("test")
    public CinemaDTO testCreateCinema(@RequestBody CinemaDTO request) {
        //        log.info("Call Create cinema API");
        var response = ICinemaService.createCinema(request);
        return response;
    }

    /**
     * Tạo mới rạp chiếu phim
     *
     * @param request DTO chứa thông tin rạp cần tạo
     * @return CinemaDTO đã được lưu
     */
    @PostMapping
    public ApiResponse<CinemaDTO> createCinema(@ValidCinemaDTO @RequestBody CinemaDTO request, BindingResult
            result) throws Exception {
        log.info("Call Create cinema API");
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(error -> error.getDefaultMessage())
                    .toList();
            return ApiResponse.<CinemaDTO>builder()
                    .code(HttpStatus.BAD_REQUEST.value())
                    .message(String.join(";", errorMessages))
                    .build();
        }
        var response = ICinemaService.createCinema(request);
        return ApiResponse.<CinemaDTO>builder()
                .code(HttpStatus.CREATED.value())
                .message("Cinema is created")
                .data(response)
                .build();
    }

    /**
     * Cập nhật thông tin rạp chiếu phim
     *
     * @param cinemaId ID của rạp cần cập nhật
     * @param request  DTO chứa thông tin cần cập nhật
     * @return CinemaDTO sau khi cập nhật
     */
    @PutMapping
    public ApiResponse<CinemaDTO> updateCinema(@RequestParam(required = true) UUID cinemaId, @RequestBody CinemaDTO request, BindingResult result) throws Exception {
        log.info("Call Update cinema API");

        var response = ICinemaService.updateCinema(cinemaId, request);
        return ApiResponse.<CinemaDTO>builder()
                .code(HttpStatus.OK.value())
                .message("Update cinema Successfully")
                .data(response)
                .build();
    }

    /**
     * Xóa mềm rạp chiếu phim bằng cách set status = INACTIVE
     *
     * @param cinemaId ID của rạp cần xóa
     * @return CinemaDTO sau khi xóa mềm
     */
    @DeleteMapping
    public ApiResponse<CinemaDTO> deleteCinema(@RequestBody UUID cinemaId) {
        log.info("Call Delete cinema API");
        var response = ICinemaService.deleteCinema(cinemaId);
        return ApiResponse.<CinemaDTO>builder()
                .code(HttpStatus.OK.value())
                .message("Delete cinema Successfully")
                .data(response)
                .build();
    }

    /**
     * Lấy thông tin chi tiết 1 rạp theo ID
     *
     * @param cinemaId ID rạp
     * @return CinemaDTO chứa thông tin chi tiết rạp
     */
    @GetMapping("{id}")
    public ApiResponse<CinemaDTO> getCinema(@PathVariable("id") UUID cinemaId) {

        var response = ICinemaService.getCinemaById(cinemaId);

        return ApiResponse
                .<CinemaDTO>builder()
                .code(HttpStatus.OK.value())
                .message("Get cinema with Id " + cinemaId + " successfully")
                .data(response)
                .build();
    }

    /**
     * Lấy danh sách tất cả các rạp
     *
     * @return List<CinemaDTO> danh sách rạp
     */
    @GetMapping
    public ApiResponse<List<CinemaDTO>> getCinemas(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int limit) {

        var response = ICinemaService.getCinemas(page, limit);

        return ApiResponse
                .<List<CinemaDTO>>builder()
                .code(HttpStatus.OK.value())
                .message("Get list cinemas successfully")
                .data(response)
                .build();
    }
}
