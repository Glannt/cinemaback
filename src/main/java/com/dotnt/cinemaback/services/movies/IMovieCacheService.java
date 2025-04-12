package com.dotnt.cinemaback.services.movies;

import com.dotnt.cinemaback.dto.response.MovieResponseDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface IMovieCacheService {

    MovieResponseDTO getMovieCacheById(UUID movieId);

    Page<MovieResponseDTO> getMovies(int page, int limit) throws JsonProcessingException;

    List<MovieResponseDTO> getTop4MovieCache(String status) throws JsonProcessingException;
}
