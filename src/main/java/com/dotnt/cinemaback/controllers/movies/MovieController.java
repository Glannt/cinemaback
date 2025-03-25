package com.dotnt.cinemaback.controllers.movies;

import com.dotnt.cinemaback.dto.request.MovieRequestDTO;
import com.dotnt.cinemaback.dto.response.ApiResponse;
import com.dotnt.cinemaback.dto.response.MovieResponseDTO;
import com.dotnt.cinemaback.services.IMovieService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/movies")
@RequiredArgsConstructor
@Slf4j(topic = "MOVIE_CONTROLLER")
public class MovieController {
    private final IMovieService movieService;

    /**
     * Create a new movie
     *
     * @param request DTO containing movie information
     * @return MovieResponse saved
     */
    @PostMapping
    public ApiResponse<MovieResponseDTO> createMovie(@RequestBody MovieRequestDTO request, BindingResult result) {
        if (result.hasErrors()) {
            log.error("Validation error: {}", result.getAllErrors());
            return ApiResponse.<MovieResponseDTO>builder()
                    .code(HttpStatus.BAD_REQUEST.value())
                    .message("Validation error")
                    .build();
        }
        var response = movieService.createMovie(request);
        return ApiResponse.<MovieResponseDTO>builder()
                .code(HttpStatus.CREATED.value())
                .message("Movie is created")
                .data(response)
                .build();
    }

    /**
     * Update movie information
     *
     * @param movieId ID of the movie to update
     * @param request DTO containing updated information
     * @return MovieResponse updated
     */
    @PutMapping("{id}")
    public ApiResponse<MovieResponseDTO> updateMovie(@PathVariable("id") UUID movieId, @RequestBody MovieRequestDTO request) {
        var response = movieService.updateMovie(movieId, request);
        return ApiResponse.<MovieResponseDTO>builder()
                .code(HttpStatus.OK.value())
                .message("Movie is updated")
                .data(response)
                .build();
    }

    /**
     * Get movie details by ID
     *
     * @param movieId ID of the movie
     * @return MovieResponse containing movie details
     */
    @GetMapping("{id}")
    public ApiResponse<MovieResponseDTO> getMovieById(@PathVariable("id") UUID movieId) {
        var response = movieService.getMovie(movieId.toString());
        return ApiResponse.<MovieResponseDTO>builder()
                .code(HttpStatus.OK.value())
                .message("Movie is fetched")
                .data(response)
                .build();
    }

    /**
     * Get list of movies
     *
     * @return list of movies
     */
    @GetMapping
    public ApiResponse<List<MovieResponseDTO>> getMovies(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int limit) {
        var response = movieService.getAllMovies(page, limit);
        return ApiResponse.<List<MovieResponseDTO>>builder()
                .code(HttpStatus.OK.value())
                .message("Movies are fetched")
                .data(response)
                .build();
    }

    /**
     * Get list of movies by genre name
     *
     * @param genreName name of the genre
     * @return list of movies
     */
    @GetMapping("/genre/{name}")
    public ApiResponse<List<MovieResponseDTO>> getMoviesByGenreName(@PathVariable("name") String genreName) {
        var response = movieService.getMoviesByGenreName(genreName);
        return ApiResponse.<List<MovieResponseDTO>>builder()
                .code(HttpStatus.OK.value())
                .message("Movies are fetched by genre")
                .data(response)
                .build();
    }

    /**
     * Get list of movies by showing status
     *
     * @return list of movies
     */
    @GetMapping("/showing")
    public ApiResponse<List<MovieResponseDTO>> getMoviesByShowingStatus() {
        var response = movieService.getMoviesByShowingStatus();
        return ApiResponse.<List<MovieResponseDTO>>builder()
                .code(HttpStatus.OK.value())
                .message("Movies currently showing are fetched")
                .data(response)
                .build();
    }

    /**
     * Get list of upcoming movies
     *
     * @return list of movies
     */
    @GetMapping("/upcoming")
    public ApiResponse<List<MovieResponseDTO>> getMoviesByUpComingStatus() {
        var response = movieService.getMoviesByUpComingStatus();
        return ApiResponse.<List<MovieResponseDTO>>builder()
                .code(HttpStatus.OK.value())
                .message("Upcoming movies are fetched")
                .data(response)
                .build();
    }

    /**
     * Get list of types by movie ID
     *
     * @param movieId ID of the movie
     * @return list of types
     */
    @GetMapping("{id}/types")
    public ApiResponse<List<String>> getTypesByMovieId(@PathVariable("id") UUID movieId) {
        var response = movieService.getTypesByMovieId(movieId);
        return ApiResponse.<List<String>>builder()
                .code(HttpStatus.OK.value())
                .message("Types are fetched")
                .data(response)
                .build();
    }
}
