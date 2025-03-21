package com.dotnt.cinemaback.services.impl;

import com.dotnt.cinemaback.dto.request.ShowTimeRequestDTO;
import com.dotnt.cinemaback.dto.response.ShowTimeResponseDTO;
import com.dotnt.cinemaback.exception.AppException;
import com.dotnt.cinemaback.exception.ErrorCode;
import com.dotnt.cinemaback.mapper.ShowTimeMapper;
import com.dotnt.cinemaback.models.Hall;
import com.dotnt.cinemaback.models.Movie;
import com.dotnt.cinemaback.models.ShowTime;
import com.dotnt.cinemaback.repositories.HallRepository;
import com.dotnt.cinemaback.repositories.MovieRepository;
import com.dotnt.cinemaback.repositories.ShowTimeRepository;
import com.dotnt.cinemaback.services.IShowTimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShowTimeService implements IShowTimeService {
    private final MovieRepository movieRepository;
    private final HallRepository hallRepository;
    private final ShowTimeMapper showTimeMapper;
    private final ShowTimeRepository showTimeRepository;

    @Override
    public ShowTimeResponseDTO createShowTime(ShowTimeRequestDTO dto) {
        Movie movie = movieRepository.findById(dto.getMovieId())
                .orElseThrow(() -> new RuntimeException("Movie not found"));
        Hall hall = hallRepository.findById(dto.getHallId())
                .orElseThrow(() -> new RuntimeException("Hall not found"));
//        ShowTime showTime = ShowTime.builder()
//                .movie(movie)
//                .hall(hall)
//                .startTime(dto.getStartTime())
//                .endTime(dto.getEndTime())
//                .ticketPrice(dto.getPrice())
//                .build();
        ShowTime showTime = showTimeMapper.convertToEntity(dto, movie, hall);
        showTimeRepository.save(showTime);

        return showTimeMapper.convertToResponseDTO(showTime);
    }

    @Override
    public List<ShowTimeResponseDTO> getAllShowTimes(int page, int limit) {
        return showTimeRepository.findAll().stream()
                .skip((page - 1) * limit)
                .limit(limit)
                .map(showTimeMapper::convertToResponseDTO)
                .toList();
    }

    @Override
    public List<ShowTimeResponseDTO> getShowTimesByMovie(UUID movieId) {
        return showTimeRepository
                .findAll()
                .stream()
                .filter(showTime -> showTime.getMovie().getId().equals(movieId))
                .map(showTimeMapper::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ShowTimeResponseDTO> getShowTimesByCinema(UUID cinemaId) {
//        return showTimeRepository.findByHall_Cinema_Id(cinemaId).stream()
//                .map(showTimeMapper::convertToResponseDTO)
//                .toList();
        return showTimeRepository
                .findAll()
                .stream()
                .filter(showTime -> showTime.getMovie().getId().equals(cinemaId))
                .map(showTimeMapper::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ShowTimeResponseDTO updateShowTime(UUID id, ShowTimeRequestDTO dto) {
        ShowTime showTime = showTimeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ShowTime not found"));

        Movie movie = movieRepository.findById(dto.getMovieId())
                .orElseThrow(() -> new RuntimeException("Movie not found"));
        Hall hall = hallRepository.findById(dto.getHallId())
                .orElseThrow(() -> new RuntimeException("Hall not found"));

        // Update fields
        showTime.setMovie(movie);
        showTime.setHall(hall);
        showTime.setStartTime(dto.getStartTime());
        showTime.setEndTime(dto.getEndTime());
        showTime.setTicketPrice(dto.getPrice());

        showTimeRepository.save(showTime);
        return showTimeMapper.convertToResponseDTO(showTime);
    }

    @Override
    public void deleteShowTime(UUID id) {

    }
    @Override
    public List<ShowTimeResponseDTO> getShowTimesByCinemaAndMovie(UUID cinemaId, UUID movieId) {
        return showTimeRepository.findAll().stream()
                .filter(showTime -> showTime.getHall().getCinema().getId().equals(cinemaId))
                .filter(showTime -> showTime.getMovie().getId().equals(movieId))
                .map(showTimeMapper::convertToResponseDTO)
                .collect(Collectors.toList());
    }
    @Override
    public List<ShowTimeResponseDTO> getShowTimesByCinemaAndMovieAndShowDate(UUID cinemaId, UUID movieId, LocalDate showDate) {
        return showTimeRepository.findAll().stream()
                .filter(showTime -> showTime.getHall().getCinema().getId().equals(cinemaId))
                .filter(showTime -> showTime.getMovie().getId().equals(movieId))
                .filter(showTime -> showTime.getShowDate().equals(showDate))
                .map(showTimeMapper::convertToResponseDTO)
                .collect(Collectors.toList());
    }
    @Override
    public String getProjectionTypeByShowTimeId(UUID id) {
        ShowTime showTime = showTimeRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ID_NOT_FOUND));
        Hall hall = hallRepository.findById(showTime.getHall().getId())
                .orElseThrow(() -> new AppException(ErrorCode.ID_NOT_FOUND));
        return hall.getProjectionType().toString();
    }

}
