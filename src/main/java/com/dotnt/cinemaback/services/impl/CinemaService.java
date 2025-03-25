package com.dotnt.cinemaback.services.impl;

import com.dotnt.cinemaback.constants.enums.CinemaStatus;
import com.dotnt.cinemaback.dto.CinemaDTO;
import com.dotnt.cinemaback.exception.AppException;
import com.dotnt.cinemaback.exception.ErrorCode;
import com.dotnt.cinemaback.mapper.CinemaMapper;
import com.dotnt.cinemaback.models.Cinema;
import com.dotnt.cinemaback.repositories.CinemaRepository;
import com.dotnt.cinemaback.services.ICinemaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "CINEMA-SERIVCE")
public class CinemaService implements ICinemaService {
    private final CinemaRepository cinemaRepository;
    private final CinemaMapper cinemaMapper;


    @Override
    @Transactional
    public CinemaDTO createCinema(CinemaDTO request) {

        Cinema newCinema = Cinema
                .builder()
                .name(request.getName())
                .address(request.getAddress())
                .status(CinemaStatus.valueOf(request.getStatus()))
                .halls(request.getHalls() != null ? request.getHalls() : new ArrayList<>())
                .build();

        cinemaRepository.save(newCinema);

        return cinemaMapper.toDto(newCinema);
    }

    @Override
    @Transactional
    public CinemaDTO updateCinema(UUID cinemaId, CinemaDTO request) {
        Cinema cinema = cinemaRepository.findCinemaById(cinemaId)
                .orElseThrow(() -> new AppException(ErrorCode.ID_NOT_FOUND));

        if (request.getName() != null) {
            cinema.setName(request.getName());
        }

        if (request.getStatus() != null) {
            cinema.setStatus(CinemaStatus.valueOf(request.getStatus()));
        }


        if (request.getHalls() != null) {
            cinema.setHalls(request.getHalls());
        }
//        cinema.setUpdatedBy();
        cinema.setUpdatedAt(LocalDateTime.now());
        Cinema updatedCinema = cinemaRepository.save(cinema);


        return CinemaDTO
                .builder()
                .id(updatedCinema.getId())
                .name(updatedCinema.getName())
                .address(cinema.getAddress())
                .halls(updatedCinema.getHalls())
                .status(String.valueOf(updatedCinema.getStatus()))
                .build();
    }


    @Override
    @Transactional
    public CinemaDTO deleteCinema(UUID cinemaId) {
        Cinema cinema = cinemaRepository.findCinemaById(cinemaId)
                .orElseThrow(() -> new AppException(ErrorCode.ID_NOT_FOUND));

        cinema.setStatus(CinemaStatus.INACTIVE);

        Cinema deletedCinema = cinemaRepository.save(cinema);
        return CinemaDTO
                .builder()
                .id(deletedCinema.getId())
                .name(deletedCinema.getName())
                .status(String.valueOf(deletedCinema.getStatus()))
                .build();
    }

    @Override
    public CinemaDTO getCinemaById(UUID cinemaId) {
        Cinema cinema = cinemaRepository.findCinemaById(cinemaId)
                .orElseThrow(() -> new AppException(ErrorCode.ID_NOT_FOUND));

        return cinemaMapper.toDto(cinema);
    }

    @Override
    public List<CinemaDTO> getCinemas(int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        Page<Cinema> cinemas = cinemaRepository.findActiveCinemas(pageable);
        return cinemas.stream()
                .map(cinema -> CinemaDTO.builder()
                        .id(cinema.getId())
                        .name(cinema.getName())
                        .status(String.valueOf(cinema.getStatus()))
                        .address(cinema.getAddress())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public List<CinemaDTO> getCinemaWithStatusActive() {
        List<Cinema> cinemas = cinemaRepository.findByStatus(CinemaStatus.ACTIVE);
        return cinemas.stream()
                .map(cinema -> CinemaDTO.builder()
                        .id(cinema.getId())
                        .name(cinema.getName())
                        .status(cinema.getStatus().toString())
                        .address(cinema.getAddress())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public List<CinemaDTO> getCinemaWithStatusAndHaveMovieId(UUID movieId) {
        List<Cinema> cinemas = cinemaRepository.findActiveCinemasByMovieIdNative(movieId);
        return cinemas.stream()
                .map(cinema -> CinemaDTO.builder()
                        .id(cinema.getId())
                        .name(cinema.getName())
                        .status(cinema.getStatus().toString())
                        .address(cinema.getAddress())
                        .build())
                .collect(Collectors.toList());
    }
}