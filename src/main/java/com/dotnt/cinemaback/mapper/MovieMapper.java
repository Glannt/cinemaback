package com.dotnt.cinemaback.mapper;

import com.dotnt.cinemaback.dto.request.MovieRequestDTO;
import com.dotnt.cinemaback.dto.response.MovieResponseDTO;
import com.dotnt.cinemaback.models.Genre;
import com.dotnt.cinemaback.models.Movie;
import com.dotnt.cinemaback.models.MovieGenre;
import com.dotnt.cinemaback.repositories.GenreRepository;
import com.dotnt.cinemaback.repositories.MovieGenreRepository;
import com.dotnt.cinemaback.repositories.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MovieMapper {
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private MovieGenreRepository movieGenreRepository;
    public Movie toEntity(MovieResponseDTO dto) {
        if (dto == null) {
            return null;
        }

        List<MovieGenre> movieGenres = movieGenreRepository.findMovieGenresByMovieId(dto.getId());

        Movie movie = Movie
                .builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .releaseDate(LocalDate.parse(dto.getReleaseDate()))
                .movieGenres(movieGenres)
                .director(dto.getDirector())
                .duration(dto.getDuration())
                .movieImages(dto.getImages())
                .status(dto.getStatus())
                .build();

        return movie;
    }

    public MovieResponseDTO toDto(Movie entity) {
        if (entity == null) {
            return null;
        }

        MovieResponseDTO dto = new MovieResponseDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setReleaseDate(entity.getReleaseDate().toString());
        dto.setGenres(entity.getMovieGenres().stream().map(MovieGenre::getGenre).map(Genre::getName).collect(Collectors.toList()));
        dto.setDirector(entity.getDirector());
        dto.setDuration(entity.getDuration());
        dto.setImages(entity.getMovieImages());
        dto.setStatus(entity.getStatus());

        return dto;
    }
}
