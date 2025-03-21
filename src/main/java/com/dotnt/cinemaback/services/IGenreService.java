package com.dotnt.cinemaback.services;

import com.dotnt.cinemaback.dto.response.MovieResponseDTO;
import com.dotnt.cinemaback.models.Genre;

import java.util.List;


public interface IGenreService {
    Genre getGenre(String name);

}
