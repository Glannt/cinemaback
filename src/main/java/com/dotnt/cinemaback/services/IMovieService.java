package com.dotnt.cinemaback.services;

import com.dotnt.cinemaback.dto.request.MovieRequestDTO;
import com.dotnt.cinemaback.dto.response.MovieResponseDTO;

import java.util.List;
import java.util.UUID;

public interface IMovieService {
    MovieResponseDTO createMovie(MovieRequestDTO movieRequestDTO);

    MovieResponseDTO updateMovie(UUID movieId, MovieRequestDTO movieRequestDTO);

    void deleteMovie(String movieId);

    MovieResponseDTO getMovie(String movieId);

    List<MovieResponseDTO> getAllMovies(int page, int limit);

    List<MovieResponseDTO> getMoviesByGenreName(String name);

    List<MovieResponseDTO> getMoviesByShowingStatus();

    List<MovieResponseDTO> getMoviesByUpComingStatus();

    List<String> getTypesByMovieId(UUID id);

}
