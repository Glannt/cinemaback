package com.dotnt.cinemaback.services.impl;

import com.dotnt.cinemaback.dto.request.MovieRequestDTO;
import com.dotnt.cinemaback.dto.response.MovieResponseDTO;
import com.dotnt.cinemaback.models.Genre;
import com.dotnt.cinemaback.models.Movie;
import com.dotnt.cinemaback.models.MovieGenre;
import com.dotnt.cinemaback.models.MovieImage;
import com.dotnt.cinemaback.repositories.GenreRepository;
import com.dotnt.cinemaback.repositories.MovieRepository;
import com.dotnt.cinemaback.services.IMovieService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "MOVIE-SERVICE")
public class MovieService implements IMovieService {
    private final MovieRepository movieRepository;
    private final GenreRepository genreRepository;

    @Override
    public MovieResponseDTO createMovie(MovieRequestDTO movieRequestDTO) {
        //Convert String Array Genres to List MovieGenres
        List<MovieGenre> movieGenres = movieRequestDTO.getGenres().stream()
                .map(genreName -> {
                    Genre genre = genreRepository.findByName(genreName)
                            .orElseThrow(() -> new RuntimeException("Genre not found: " + genreName));
                    return MovieGenre.builder()
                            .genre(genre) // chỉ set genre trước
                            .build();
                })
                .collect(Collectors.toList());

        // Convert MovieRequestDTO to Movie entity
        Movie movie = Movie.builder()
                .title(movieRequestDTO.getTitle())
                .description(movieRequestDTO.getDescription())
                .duration(movieRequestDTO.getDuration())
                .director(movieRequestDTO.getDirector())
                .releaseDate(LocalDate.parse(movieRequestDTO.getReleaseDate()))
                .movieImages(List.of(
                        MovieImage.builder()
                                .url(movieRequestDTO.getPoster())
                                .title("POSTER")
                                .build(),
                        MovieImage.builder()
                                .url(movieRequestDTO.getTrailer())
                                .title("TRAILER")
                                .build()
                ))
                .movieGenres(movieGenres)
                .status(movieRequestDTO.getStatus())
                .price(movieRequestDTO.getPrice())
                .rating(movieRequestDTO.getRating())
                .build();

        Movie finalMovie = movie;
        movie.getMovieGenres().forEach(mg -> mg.setMovie(finalMovie));


        // Save the movie entity to the repository
        movie = movieRepository.save(finalMovie);

        // Convert the saved Movie entity to MovieResponseDTO
        MovieResponseDTO movieResponseDTO = MovieResponseDTO.builder()
                .id(finalMovie.getId())
                .title(finalMovie.getTitle())
                .description(finalMovie.getDescription())
                .duration(finalMovie.getDuration())
                .director(finalMovie.getDirector())
                .releaseDate(finalMovie.getReleaseDate().toString())
                .poster(finalMovie.getMovieImages().stream()
                        .filter(movieImage -> movieImage.getTitle().equals("POSTER"))
                        .findFirst()
                        .map(MovieImage::getUrl)
                        .orElse(""))
                .trailer(finalMovie.getMovieImages().stream()
                        .filter(movieImage -> movieImage.getTitle().equals("TRAILER"))
                        .findFirst()
                        .map(MovieImage::getUrl)
                        .orElse(""))
                .genres(finalMovie.getMovieGenres().stream().map(movieGenre -> movieGenre.getGenre().getName()).collect(Collectors.toList()))
                .status(movie.getStatus())
                .build();

        // Log the creation of the movie
        log.info("Created movie: {}", movieResponseDTO);

        // Return the created movie
        return movieResponseDTO;
    }

    @Override
    public MovieResponseDTO updateMovie(UUID movieId, MovieRequestDTO movieRequestDTO) {
        if (movieRequestDTO == null) {
            throw new IllegalArgumentException("MovieRequestDTO cannot be null");
        }
        // Fetch the existing movie by ID
        Movie existingMovie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found"));
        if (existingMovie.getStatus().equals("INACTIVE")) {
            throw new RuntimeException("Movie is deleted");
        }
        // Update the movie details
        if (movieRequestDTO.getTitle() != null) {
            existingMovie.setTitle(movieRequestDTO.getTitle());
        }
        if (movieRequestDTO.getDescription() != null) {
            existingMovie.setDescription(movieRequestDTO.getDescription());
        }
        if (movieRequestDTO.getDuration() != null) {
            existingMovie.setDuration(movieRequestDTO.getDuration());
        }
        if (movieRequestDTO.getDirector() != null) {
            existingMovie.setDirector(movieRequestDTO.getDirector());
        }
        if (movieRequestDTO.getGenres() != null) {
            List<MovieGenre> movieGenres = movieRequestDTO.getGenres().stream()
                    .map(genreName -> {
                        Genre genre = Genre.builder().name(genreName).build();
                        return MovieGenre.builder().movie(existingMovie).genre(genre).build();
                    })
                    .toList();
            existingMovie.setMovieGenres(movieGenres);
        }
        if (movieRequestDTO.getReleaseDate() != null) {
            existingMovie.setReleaseDate(LocalDate.parse(movieRequestDTO.getReleaseDate()));
        }
        if (movieRequestDTO.getPoster() != null) {
            MovieImage posterImage = MovieImage.builder()
                    .url(movieRequestDTO.getPoster())
                    .title("POSTER")
                    .movie(existingMovie)
                    .build();
            existingMovie.setMovieImages(List.of(posterImage));
        }

        if (movieRequestDTO.getTrailer() != null) {
            MovieImage trailer = MovieImage.builder()
                    .url(movieRequestDTO.getTrailer())
                    .title("TRAILER")
                    .movie(existingMovie)
                    .build();
            existingMovie.setMovieImages(List.of(trailer));
        }

        if (movieRequestDTO.getStatus() != null) {
            existingMovie.setStatus(movieRequestDTO.getStatus());
        }
//        existingMovie.setCast(movieRequestDTO.getCast());
        existingMovie.setReleaseDate(LocalDate.parse(movieRequestDTO.getReleaseDate()));

        // Save the updated movie
        movieRepository.save(existingMovie);

        // Convert the updated movie to MovieResponseDTO
        return MovieResponseDTO.builder()
                .id(existingMovie.getId())
                .title(existingMovie.getTitle())
                .description(existingMovie.getDescription())
                .duration(existingMovie.getDuration())
                .director(existingMovie.getDirector())
//                .cast(existingMovie.getCast())
                .genres(existingMovie.getMovieGenres().stream().map(movieGenre -> movieGenre.getGenre().getName()).toList())
                .releaseDate(existingMovie.getReleaseDate().toString())
                .poster(existingMovie.getMovieImages().stream()
                        .filter(movieImage -> movieImage.getTitle().equals("POSTER"))
                        .findFirst()
                        .map(MovieImage::getUrl)
                        .orElse(""))
                .trailer(existingMovie.getMovieImages().stream()
                        .filter(movieImage -> movieImage.getTitle().equals("TRAILER"))
                        .findFirst()
                        .map(MovieImage::getUrl)
                        .orElse(""))
                .status(existingMovie.getStatus())
                .build();
    }

    @Override
    public void deleteMovie(String movieId) {
        // Fetch the existing movie by ID
        Movie existingMovie = movieRepository.findById(UUID.fromString(movieId))
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        // Delete the movie
        existingMovie.setStatus("INACTIVE");
        movieRepository.save(existingMovie);

        // Log the deletion of the movie
        log.info("Deleted movie: {}", existingMovie);

    }

    @Override
    public MovieResponseDTO getMovie(String movieId) {
        return movieRepository.findById(UUID.fromString(movieId))
                .filter(movie -> !movie.getStatus().equals("INACTIVE"))
                .map(movie -> MovieResponseDTO.builder()
                        .id(movie.getId())
                        .title(movie.getTitle())
                        .description(movie.getDescription())
                        .duration(movie.getDuration())
                        .director(movie.getDirector())
                        .genres(movie.getMovieGenres().stream().map(movieGenre -> movieGenre.getGenre().getName()).toList())
                        .releaseDate(movie.getReleaseDate().toString())
                        .poster(movie.getMovieImages().stream()
                                .filter(movieImage -> movieImage.getTitle().equals("POSTER"))
                                .findFirst()
                                .map(MovieImage::getUrl)
                                .orElse(""))
                        .trailer(movie.getMovieImages().stream()
                                .filter(movieImage -> movieImage.getTitle().equals("TRAILER"))
                                .findFirst()
                                .map(MovieImage::getUrl)
                                .orElse(""))
                        .status(movie.getStatus())
                        .build())
                .orElseThrow(() -> new RuntimeException("Movie not found or is inactive"));
    }

    @Override
    public List<MovieResponseDTO> getAllMovies(int page, int limit) {

        return movieRepository.findAll().stream()
                .filter(movie -> !movie.getStatus().equals("INACTIVE"))
                .skip((page - 1) * limit)
                .limit(limit)
                .map(movie -> MovieResponseDTO.builder()
                        .id(movie.getId())
                        .title(movie.getTitle())
                        .description(movie.getDescription())
                        .duration(movie.getDuration())
                        .director(movie.getDirector())
                        .genres(movie.getMovieGenres().stream().map(movieGenre -> movieGenre.getGenre().getName()).toList())
                        .releaseDate(movie.getReleaseDate().toString())
                        .poster(movie.getMovieImages().stream()
                                .filter(movieImage -> movieImage.getTitle().equals("POSTER"))
                                .findFirst()
                                .map(MovieImage::getUrl)
                                .orElse(""))
                        .trailer(movie.getMovieImages().stream()
                                .filter(movieImage -> movieImage.getTitle().equals("TRAILER"))
                                .findFirst()
                                .map(MovieImage::getUrl)
                                .orElse(""))
                        .status(movie.getStatus())
                        .build())
                .toList();
    }
}
