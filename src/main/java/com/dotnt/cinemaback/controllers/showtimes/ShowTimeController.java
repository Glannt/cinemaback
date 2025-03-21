package com.dotnt.cinemaback.controllers.showtimes;

import com.dotnt.cinemaback.dto.request.ShowTimeRequestDTO;
import com.dotnt.cinemaback.dto.response.ApiResponse;
import com.dotnt.cinemaback.dto.response.ShowTimeResponseDTO;
import com.dotnt.cinemaback.services.IShowTimeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/showtimes")
@RequiredArgsConstructor
@Slf4j(topic = "SHOWTIME_SERVICE")
public class ShowTimeController {
    private final IShowTimeService showTimeService;

    /**
     * Create a new showtime
     *
     * @param dto DTO containing showtime information
     * @return ShowTimeResponse saved
     */
    @PostMapping
    public ApiResponse<ShowTimeResponseDTO> createShowTime(@RequestBody ShowTimeRequestDTO dto) {
        ShowTimeResponseDTO response = showTimeService.createShowTime(dto);
        return ApiResponse.<ShowTimeResponseDTO>builder()
                .code(HttpStatus.CREATED.value())
                .message("Showtime is created")
                .data(response)
                .build();
    }

    /**
     * Get list of showtimes
     *
     * @param page page number
     * @param limit number of items per page
     * @return list of showtimes
     */
    @GetMapping
    public ApiResponse<List<ShowTimeResponseDTO>> getAllShowTimes(@RequestParam int page, @RequestParam int limit) {
        List<ShowTimeResponseDTO> response = showTimeService.getAllShowTimes(page, limit);
        return ApiResponse.<List<ShowTimeResponseDTO>>builder()
                .code(HttpStatus.OK.value())
                .message("Showtimes are fetched ")
                .data(response)
                .build();
    }

    /**
     * Get showtimes by cinema ID and movie ID
     *
     * @param cinemaId ID of the cinema
     * @param movieId ID of the movie
     * @return list of showtimes
     */
    @GetMapping("/cinema/{cinemaId}/movie/{movieId}")
    public ApiResponse<List<ShowTimeResponseDTO>> getShowTimesByCinemaAndMovie(
            @PathVariable("cinemaId") UUID cinemaId, @PathVariable("movieId") UUID movieId) {
        List<ShowTimeResponseDTO> response = showTimeService.getShowTimesByCinemaAndMovie(cinemaId, movieId);
        return ApiResponse.<List<ShowTimeResponseDTO>>builder()
                .code(HttpStatus.OK.value())
                .message("Showtimes are fetched by cinema and movie")
                .data(response)
                .build();
    }
    /**
     * Get showtimes by movie ID
     *
     * @param movieId ID of the movie
     * @return list of showtimes
     */
    @GetMapping("/movie/{movieId}")
    public ApiResponse<List<ShowTimeResponseDTO>> getShowTimesByMovie(@PathVariable("movieId") UUID movieId) {
        List<ShowTimeResponseDTO> response = showTimeService.getShowTimesByMovie(movieId);
        return ApiResponse.<List<ShowTimeResponseDTO>>builder()
                .code(HttpStatus.OK.value())
                .message("Showtimes are fetched by movie")
                .data(response)
                .build();
    }

    /**
     * Get showtimes by cinema ID
     *
     * @param cinemaId ID of the cinema
     * @return list of showtimes
     */
    @GetMapping("/cinema/{cinemaId}")
    public ApiResponse<List<ShowTimeResponseDTO>> getShowTimesByCinema(@PathVariable("cinemaId") UUID cinemaId) {
        List<ShowTimeResponseDTO> response = showTimeService.getShowTimesByCinema(cinemaId);
        return ApiResponse.<List<ShowTimeResponseDTO>>builder()
                .code(HttpStatus.OK.value())
                .message("Showtimes are fetched by cinema")
                .data(response)
                .build();
    }

    /**
     * Update showtime information
     *
     * @param id ID of the showtime to update
     * @param dto DTO containing updated information
     * @return ShowTimeResponse updated
     */
    @PutMapping("/{id}")
    public ApiResponse<ShowTimeResponseDTO> updateShowTime(@PathVariable UUID id, @RequestBody ShowTimeRequestDTO dto) {
        ShowTimeResponseDTO response = showTimeService.updateShowTime(id, dto);
        return ApiResponse.<ShowTimeResponseDTO>builder()
                .code(HttpStatus.OK.value())
                .message("Showtime is updated")
                .data(response)
                .build();
    }

    /**
     * Delete showtime by ID
     *
     * @param id ID of the showtime to delete
     * @return no content
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteShowTime(@PathVariable UUID id) {
        showTimeService.deleteShowTime(id);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.NO_CONTENT.value())
                .message("Showtime is deleted")
                .build();
    }

    /**
            * Get showtimes by cinema ID, movie ID, and show date
 *
         * @param cinemaId ID of the cinema
 * @param movieId ID of the movie
 * @param showDate Date of the show
 * @return list of showtimes
 */
    @GetMapping("/cinema/{cinemaId}/movie/{movieId}/date/{showDate}")
    public ApiResponse<List<ShowTimeResponseDTO>> getShowTimesByCinemaAndMovieAndShowDate(
            @PathVariable("cinemaId") UUID cinemaId,
            @PathVariable("movieId") UUID movieId,
            @PathVariable("showDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate showDate) {
        List<ShowTimeResponseDTO> response = showTimeService.getShowTimesByCinemaAndMovieAndShowDate(cinemaId, movieId, showDate);
        return ApiResponse.<List<ShowTimeResponseDTO>>builder()
                .code(HttpStatus.OK.value())
                .message("Showtimes are fetched by cinema, movie, and date")
                .data(response)
                .build();
    }
    /**
     * Get projection type by showtime ID
     *
     * @param id ID of the showtime
     * @return projection type
     */
    @GetMapping("/{id}/projection-type")
    public ApiResponse<String> getProjectionTypeByShowTimeId(@PathVariable UUID id) {
        String projectionType = showTimeService.getProjectionTypeByShowTimeId(id);
        return ApiResponse.<String>builder()
                .code(HttpStatus.OK.value())
                .message("Projection type is fetched")
                .data(projectionType)
                .build();
    }
}

