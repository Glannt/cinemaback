package com.dotnt.cinemaback.services;

import com.dotnt.cinemaback.dto.CinemaDTO;

import java.util.List;
import java.util.UUID;

public interface ICinemaService {
    CinemaDTO createCinema(CinemaDTO request);

    CinemaDTO updateCinema(UUID cinemaId, CinemaDTO request);

    CinemaDTO deleteCinema(UUID cinemaId);

    CinemaDTO getCinemaById(UUID cinemaId);

    List<CinemaDTO> getCinemas(int page, int limit);

}
