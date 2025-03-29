package com.dotnt.cinemaback.services.cinemas;

import com.dotnt.cinemaback.dto.CinemaDTO;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface ICinemaService {
    CinemaDTO createCinema(CinemaDTO request);

    CinemaDTO updateCinema(UUID cinemaId, CinemaDTO request);

    CinemaDTO deleteCinema(UUID cinemaId);

    CinemaDTO getCinemaById(UUID cinemaId);

    Page<CinemaDTO> getCinemas(int page, int limit);

    List<CinemaDTO> getCinemaWithStatusActive();

    List<CinemaDTO> getCinemaWithStatusAndHaveMovieId(UUID movieId);
}
