package com.dotnt.cinemaback.services.impl;

import com.dotnt.cinemaback.dto.response.MovieResponseDTO;
import com.dotnt.cinemaback.models.Genre;
import com.dotnt.cinemaback.models.MovieImage;
import com.dotnt.cinemaback.repositories.GenreRepository;
import com.dotnt.cinemaback.repositories.MovieRepository;
import com.dotnt.cinemaback.services.IGenreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "GENRE_SERVICE")
public class GenreService implements IGenreService {
    private final GenreRepository genreRepository;
    private final MovieRepository movieRepository;

    @Override
    public Genre getGenre(String name) {
        Genre genre = genreRepository.findByName(name).orElseThrow();
        return genreRepository.findAll()
                .stream()
                .filter(genre1 -> genre1.getName().equalsIgnoreCase(name))
                .map(genre1 -> Genre
                        .builder()
                        .name(genre1.getName())
                        .movieGenres(genre1.getMovieGenres())
                        .build())
                .findFirst()
                .orElse(null);
    }


}
