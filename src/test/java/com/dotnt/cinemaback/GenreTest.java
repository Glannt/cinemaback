package com.dotnt.cinemaback;

import com.dotnt.cinemaback.dto.response.MovieResponseDTO;
import com.dotnt.cinemaback.models.Genre;
import com.dotnt.cinemaback.models.Movie;
import com.dotnt.cinemaback.models.MovieGenre;
import com.dotnt.cinemaback.models.MovieImage;
import com.dotnt.cinemaback.repositories.MovieRepository;
import com.dotnt.cinemaback.services.IMovieService;
import com.dotnt.cinemaback.services.impl.MovieService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

public class GenreTest {
    private static final Logger log = LoggerFactory.getLogger(GenreTest.class);
    @Mock
    private MovieRepository movieRepository;

    @InjectMocks
    private MovieService movieService;

    @Test
    void testGetMoviesByGenreName() {
        // Arrange - Tạo dữ liệu giả
        Genre actionGenre = Genre.builder().name("Action").build();
        MovieGenre movieGenre = MovieGenre.builder().genre(actionGenre).build();

        MovieImage poster = MovieImage.builder().title("POSTER").url("poster-url").build();
        MovieImage trailer = MovieImage.builder().title("TRAILER").url("trailer-url").build();

        Movie movie = Movie.builder()
                .title("Test Movie")
                .description("Test Description")
                .duration(120)
                .director("Test Director")
                .releaseDate(LocalDate.of(2024, 1, 1))
                .status("ACTIVE")
                .movieGenres(List.of(movieGenre))
                .movieImages(List.of(poster, trailer))
                .build();

        // Đảm bảo bidirectional
        movieGenre.setMovie(movie);


        // Giả lập repository trả về list movie
        when(movieRepository.findByMovieGenres_Genre_NameIgnoreCase("Action"))
                .thenReturn(List.of(movie));

        // Act
        List<MovieResponseDTO> result = movieService.getMoviesByGenreName("Action");
        log.info("Result: {}", result);
        // Assert
        assertEquals(1, result.size());
        MovieResponseDTO response = result.get(0);
        assertEquals("Test Movie", response.getTitle());
        assertEquals("Test Description", response.getDescription());
        assertEquals("poster-url", response.getPoster());
        assertEquals("trailer-url", response.getTrailer());
        assertTrue(response.getGenres().contains("Action"));

        // Verify repository được gọi đúng 1 lần
        verify(movieRepository, times(1)).findByMovieGenres_Genre_NameIgnoreCase("Action");
    }
}
