package com.dotnt.cinemaback.services.movies.impl;

import com.dotnt.cinemaback.dto.request.MovieImageDTO;
import com.dotnt.cinemaback.dto.request.MovieRequestDTO;
import com.dotnt.cinemaback.dto.response.MovieResponseDTO;
import com.dotnt.cinemaback.exception.AppException;
import com.dotnt.cinemaback.exception.ErrorCode;
import com.dotnt.cinemaback.models.Genre;
import com.dotnt.cinemaback.models.Movie;
import com.dotnt.cinemaback.models.MovieGenre;
import com.dotnt.cinemaback.models.MovieImage;
import com.dotnt.cinemaback.repositories.GenreRepository;
import com.dotnt.cinemaback.repositories.MovieImageRepository;
import com.dotnt.cinemaback.repositories.MovieRepository;
import com.dotnt.cinemaback.services.movies.IMovieService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "MOVIE-SERVICE")
public class MovieService implements IMovieService {
    private final MovieRepository movieRepository;
    private final GenreRepository genreRepository;
    private final MovieImageRepository movieImageRepository;


    //add movie
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
                .id(movie.getId())
                .title(movie.getTitle())
                .description(movie.getDescription())
                .duration(movie.getDuration())
                .director(movie.getDirector())
                .releaseDate(movie.getReleaseDate().toString())
                .images( new ArrayList<>() )
                .genres(movie
                        .getMovieGenres()
                        .stream()
                        .map(movieGenre -> movieGenre
                                .getGenre()
                                .getName()).collect(Collectors.toList()))
                .status(movie.getStatus())
                .build();

        // Log the creation of the movie
        log.info("Created movie: {}", movieResponseDTO);

        // Return the created movie
        return movieResponseDTO;
    }

    //update movie
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
//        if (movieRequestDTO.getPoster() != null) {
//            MovieImage posterImage = MovieImage.builder()
//                    .url(movieRequestDTO.getPoster())
//                    .title("POSTER")
//                    .movie(existingMovie)
//                    .build();
//            existingMovie.setMovieImages(List.of(posterImage));
//        }
//
//        if (movieRequestDTO.getTrailer() != null) {
//            MovieImage trailer = MovieImage.builder()
//                    .url(movieRequestDTO.getTrailer())
//                    .title("TRAILER")
//                    .movie(existingMovie)
//                    .build();
//            existingMovie.setMovieImages(List.of(trailer));
//        }

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

                .images(existingMovie.getMovieImages().stream().toList())
                .status(existingMovie.getStatus())
                .build();
    }

    //delete movie
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

    //get movie by id
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
                        .images(movie.getMovieImages().stream().toList())
                        .status(movie.getStatus())
                        .build())
                .orElseThrow(() -> new RuntimeException("Movie not found or is inactive"));
    }

    //get all movies(admin, manager)
    @Override
    public List<MovieResponseDTO> getAllMovies(int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by("createdAt").descending());
        Page<Movie> moviePage = movieRepository.findByStatusNot("INACTIVE", pageable);

        return moviePage.stream()
                .map(movie -> MovieResponseDTO.builder()
                        .id(movie.getId())
                        .title(movie.getTitle())
                        .description(movie.getDescription())
                        .duration(movie.getDuration())
                        .director(movie.getDirector())
                        .genres(movie.getMovieGenres().stream()
                                .map(movieGenre -> movieGenre.getGenre().getName())
                                .toList())
                        .releaseDate(movie.getReleaseDate().toString())
                        .images(movie.getMovieImages().stream().toList())
                        .status(movie.getStatus())
                        .build())
                .toList();
    }

    //get movie by genres
    @Override
    public List<MovieResponseDTO> getMoviesByGenreName(String name) {
        return movieRepository.findByMovieGenres_Genre_NameIgnoreCase(name).stream()
                .map(movie -> MovieResponseDTO.builder()
                        .id(movie.getId())
                        .title(movie.getTitle())
                        .description(movie.getDescription())
                        .duration(movie.getDuration())
                        .director(movie.getDirector())
                        .genres(movie.getMovieGenres().stream().map(movieGenre -> movieGenre.getGenre().getName()).toList())
                        .releaseDate(movie.getReleaseDate().toString())
                        .images(movie.getMovieImages().stream().toList())
                        .status(movie.getStatus())
                        .build())
                .toList();
    }

    //get movies showing
    @Override
    public List<MovieResponseDTO> getMoviesByShowingStatus() {
        List<Movie> showingMovies = movieRepository.findByStatusIgnoreCase("Showing");

        return showingMovies.stream()
                .map(movie -> MovieResponseDTO.builder()
                        .id(movie.getId())
                        .title(movie.getTitle())
                        .description(movie.getDescription())
                        .duration(movie.getDuration())
                        .director(movie.getDirector())
                        .genres(movie.getMovieGenres().stream()
                                .map(movieGenre -> movieGenre.getGenre().getName())
                                .toList())
                        .releaseDate(movie.getReleaseDate().toString())
                        .images(movie.getMovieImages().stream().toList())
                        .status(movie.getStatus())
                        .build())
                .toList();
    }

    @Override
    public List<MovieResponseDTO> getMoviesByUpComingStatus() {
        List<Movie> upcomingMovies = movieRepository.findByStatusIgnoreCase("Upcoming");

        return upcomingMovies.stream()
                .map(movie -> MovieResponseDTO.builder()
                        .id(movie.getId())
                        .title(movie.getTitle())
                        .description(movie.getDescription())
                        .duration(movie.getDuration())
                        .director(movie.getDirector())
                        .genres(movie.getMovieGenres().stream()
                                .map(movieGenre -> movieGenre.getGenre().getName())
                                .toList())
                        .releaseDate(movie.getReleaseDate().toString())
                        .images(movie.getMovieImages().stream().toList())
                        .status(movie.getStatus())
                        .build())
                .toList();
    }

    @Override
    public List<String> getTypesByMovieId(UUID id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found"));
        return movie.getMovieGenres().stream()
                .map(movieGenre -> movieGenre.getGenre().getName())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public MovieImage createMovieImage(UUID movieId, MovieImageDTO request) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new AppException(ErrorCode.ID_NOT_FOUND));

        MovieImage newMovieImage = MovieImage.builder()
                .url(request.getUrl())
                .title(request.getTitle())
                .movie(movie)
                .build();

        movie.getMovieImages().add(newMovieImage);
        movieRepository.save(movie);
//        movieImageRepository.save(newMovieImage);
        return newMovieImage;
    }

    @Override
    public MovieImage deleteMovieImage(UUID movieId, UUID imageId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new AppException(ErrorCode.ID_NOT_FOUND));

        MovieImage movieImage = movie.getMovieImages().stream()
                .filter(image -> image.getId().equals(imageId))
                .findFirst()
                .orElseThrow(() -> new AppException(ErrorCode.ID_NOT_FOUND));

        movie.getMovieImages().remove(movieImage);
        movieRepository.save(movie);

        return movieImage;
    }

//    public List<MovieResponseDTO> getTop4MoviesByReleaseDate(String status) throws JsonProcessingException {
//        return movieCacheService.getTop4MovieCache(status);
//    }
}
