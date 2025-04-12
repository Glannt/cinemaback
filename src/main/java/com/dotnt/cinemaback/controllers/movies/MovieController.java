package com.dotnt.cinemaback.controllers.movies;

import com.dotnt.cinemaback.dto.request.MovieImageDTO;
import com.dotnt.cinemaback.dto.request.MovieRequestDTO;
import com.dotnt.cinemaback.dto.response.ApiResponse;
import com.dotnt.cinemaback.dto.response.MovieResponseDTO;
import com.dotnt.cinemaback.models.MovieImage;
import com.dotnt.cinemaback.services.movies.IMovieCacheService;
import com.dotnt.cinemaback.services.movies.IMovieService;
import com.dotnt.cinemaback.utils.FileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/movies")
@RequiredArgsConstructor
@Slf4j(topic = "MOVIE_CONTROLLER")
public class MovieController {
    private final IMovieService movieService;

    private final IMovieCacheService movieCacheService;

    /**
     * Create a new movie
     *
     * @param request DTO containing movie information
     * @return MovieResponse saved
     */
    @PostMapping
    public ResponseEntity<ApiResponse<MovieResponseDTO>> createMovie(@RequestBody MovieRequestDTO request, BindingResult result) {
        if (result.hasErrors()) {
            log.error("Validation error: {}", result.getAllErrors());
            return ResponseEntity.badRequest().body(ApiResponse.<MovieResponseDTO>builder()
                    .code(HttpStatus.BAD_REQUEST.value())
                    .message("Validation error")
                    .build());
        }
        var response = movieService.createMovie(request);
         return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.<MovieResponseDTO>builder()
                .code(HttpStatus.CREATED.value())
                .message("Movie is created")
                .data(response)
                .build());
    }


    /**
     * Upload movie images
     * @param movieId ID of the movie
     * @param poster Poster image
//     * @param trailer Trailer video (optional)
     * @param files Additional image files
     * @return ApiResponse with list of MovieImage
     */

    @PostMapping(value = "uploads/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<List<MovieImage>>> createMovieImage(@PathVariable("id") UUID movieId,
                                                          @RequestParam(value = "poster", required = false) MultipartFile poster,
//                                                          @ModelAttribute("trailer") MultipartFile trailer,
                                                          @RequestParam(value = "files", required = false) List<MultipartFile> files) throws IOException {

        List<MovieImage> images = new ArrayList<>();
        long size  = poster.getSize();
        // Handle poster if provided
        if (!poster.isEmpty() || poster.getSize() > 0) {
            ApiResponse<?> check = FileUtils.validateImageFile(poster);
            if (check != null) ResponseEntity.status(HttpStatus.BAD_REQUEST).body((ApiResponse<List<MovieImage>>) check);
            String filename = FileUtils.storeFile(poster);
            MovieImage image = movieService.createMovieImage(movieId, MovieImageDTO.builder()
                    .movieId(movieId)
                    .url(filename)
                    .title("poster")
                    .build());
            images.add(image);
        }

        // Handle additional image files
        if (files != null) {
            for (MultipartFile file : files) {
                if (file.isEmpty() || file.getSize() == 0) continue;

                ApiResponse<?> check = FileUtils.validateImageFile(file);
                if (check != null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((ApiResponse<List<MovieImage>>) check);

                String filename = FileUtils.storeFile(file);
                MovieImage image = movieService.createMovieImage(movieId, MovieImageDTO.builder()
                        .movieId(movieId)
                        .url(filename)
                        .title("image")
                        .build());
                images.add(image);
            }
            log.info("Uploaded {} additional images", images);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<List<MovieImage>>builder()
                        .code(HttpStatus.CREATED.value())
                        .message("Movie images uploaded successfully")
                        .data(images)
                        .build()
        );
    }

    @GetMapping("/images/{imageName}")
    public ResponseEntity<?> viewImage(@PathVariable String imageName) {
        try {
            java.nio.file.Path imagePath = Paths.get("uploads/"+imageName);
            UrlResource resource = new UrlResource(imagePath.toUri());

            if (resource.exists()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(resource);
            } else {
                log.info(imageName + " not found");
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(new UrlResource(Paths.get("uploads/notfound.jpeg").toUri()));
                //return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("Error occurred while retrieving image: " + e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Update movie information
     *
     * @param movieId ID of the movie to update
     * @param request DTO containing updated information
     * @return MovieResponse updated
     */
    @PutMapping("{id}")
    public ResponseEntity<ApiResponse<MovieResponseDTO>> updateMovie(@PathVariable("id") UUID movieId, @RequestBody MovieRequestDTO request) {
        var response = movieService.updateMovie(movieId, request);
        return ResponseEntity.ok(
                ApiResponse.<MovieResponseDTO>builder()
                        .code(HttpStatus.OK.value())
                        .message("Movie is updated")
                        .data(response)
                        .build()
        );
    }

    /**
     * Delete a movieImage
     *
     * @param movieId ID of the movie to delete
     * @param imageId ID of the image to delete
     * @return MovieImage deleted
     */
    @DeleteMapping("{id}/images/{imageId}")
    public ApiResponse<MovieImage> deleteMovieImage(@PathVariable("id") UUID movieId, @PathVariable("imageId") UUID imageId) {
        var response = movieService.deleteMovieImage(movieId, imageId);
        return ApiResponse.<MovieImage>builder()
                .code(HttpStatus.OK.value())
                .message("Movie image is deleted")
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

    /**
     * Get top 4 movies by release date
     *
     * @return list of top 4 movies
     */
    @GetMapping("/top4")
    public ApiResponse<List<MovieResponseDTO>> getTop4MoviesByReleaseDate(@RequestParam(defaultValue = "showing") String status) throws IOException {
        var response = movieCacheService.getTop4MovieCache(status);
        return ApiResponse.<List<MovieResponseDTO>>builder()
                .code(HttpStatus.OK.value())
                .message("Top 4 movies are fetched")
                .data(response)
                .build();
    }
}
