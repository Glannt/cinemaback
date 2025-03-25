package com.dotnt.cinemaback.services.impl;

import com.dotnt.cinemaback.models.Genre;
import com.dotnt.cinemaback.repositories.GenreRepository;
import com.dotnt.cinemaback.repositories.MovieRepository;
import com.dotnt.cinemaback.services.IGenreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "GENRE_SERVICE")
public class GenreService implements IGenreService {
    private final GenreRepository genreRepository;
    private final MovieRepository movieRepository;

    @Override
    public Genre getGenre(String name) {
        return genreRepository.findByName(name)
                .map(genre -> Genre.builder()
                        .name(genre.getName())
                        .movieGenres(genre.getMovieGenres())
                        .build())
                .orElseThrow(() -> new RuntimeException("Genre not found"));
    }


}
