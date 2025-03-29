package com.dotnt.cinemaback.services.cinemas;

import com.dotnt.cinemaback.dto.CinemaDTO;
import com.dotnt.cinemaback.models.cache.CinemaCache;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface ICinemaRedisService {
    CinemaDTO getCinemaCacheById(UUID cinemaId);

    Page<CinemaDTO> getAllCinemaCache(int page, int limit) throws JsonProcessingException;
}
