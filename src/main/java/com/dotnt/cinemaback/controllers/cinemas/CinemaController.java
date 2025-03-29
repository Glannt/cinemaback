package com.dotnt.cinemaback.controllers.cinemas;

import com.dotnt.cinemaback.dto.CinemaDTO;
import com.dotnt.cinemaback.dto.response.ApiResponse;
import com.dotnt.cinemaback.dto.response.ListResponse;
import com.dotnt.cinemaback.models.cache.CinemaCache;
import com.dotnt.cinemaback.services.cinemas.ICinemaRedisService;
import com.dotnt.cinemaback.services.cinemas.ICinemaService;
import com.dotnt.cinemaback.validators.ValidCinemaDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private final ICinemaRedisService cinemaCacheService;

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

        var response = cinemaCacheService.getCinemaCacheById(cinemaId);
 
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
    public ResponseEntity<ApiResponse<ListResponse<CinemaDTO>>> getCinemas(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int limit) throws JsonProcessingException {

        var cinemas = cinemaCacheService.getAllCinemaCache(page, limit);
        ListResponse<CinemaDTO> response = ListResponse.<CinemaDTO>builder()
                .currentPage(cinemas.getNumber() + 1)
                .pageSize(cinemas.getSize())
                .totalPages(cinemas.getTotalPages())
                .totalElements(cinemas.getTotalElements())
                .isFirst(cinemas.isFirst())
                .isLast(cinemas.isLast())
                .data(cinemas.getContent())
                .build();

        return ResponseEntity.ok()
                .body(ApiResponse
                        .<ListResponse<CinemaDTO>>builder()
                        .code(HttpStatus.OK.value())
                        .message("Get list cinemas successfully")
                        .data(response)
                        .build());
    }

    /**
     * Lấy danh sách các rạp có trạng thái hoạt động
     *
     * @return List<CinemaDTO> danh sách rạp có trạng thái hoạt động
     */
    @GetMapping("/active")
    public ResponseEntity<ApiResponse<ListResponse<CinemaDTO>>> getCinemasWithActiveStatus() {
        var cinemas = ICinemaService.getCinemaWithStatusActive();
        ListResponse<CinemaDTO> response = ListResponse.<CinemaDTO>builder()
                .data(cinemas)
                .build();
        return ResponseEntity
                .ok()
                .body(ApiResponse
                        .<ListResponse<CinemaDTO>>builder()
                        .code(HttpStatus.OK.value())
                        .message("Get list of active cinemas successfully")
                        .data(response)
                        .build());
    }

    /**
     * Lấy danh sách các rạp có trạng thái hoạt động và có movieId
     *
     * @param movieId ID của movie
     * @return List<CinemaDTO> danh sách rạp có trạng thái hoạt động và có movieId
     */
    @GetMapping("/active-with-movie/{movieId}")
    public ApiResponse<List<CinemaDTO>> getCinemasWithActiveStatusAndMovieId(@PathVariable("movieId") UUID movieId) {
        var response = ICinemaService.getCinemaWithStatusAndHaveMovieId(movieId);
        return ApiResponse
                .<List<CinemaDTO>>builder()
                .code(HttpStatus.OK.value())
                .message("Get list of active cinemas with movieId " + movieId + " successfully")
                .data(response)
                .build();
    }
}
