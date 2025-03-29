package com.dotnt.cinemaback.services.showtimes;

import com.dotnt.cinemaback.dto.request.ShowTimeRequestDTO;
import com.dotnt.cinemaback.dto.response.ShowTimeResponseDTO;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface IShowTimeService {
    ShowTimeResponseDTO createShowTime(ShowTimeRequestDTO dto);

    Page<ShowTimeResponseDTO> getAllShowTimes(int page, int limit);

    List<ShowTimeResponseDTO> getShowTimesByMovie(UUID movieId);

    List<ShowTimeResponseDTO> getShowTimesByCinema(UUID cinemaId);

    ShowTimeResponseDTO updateShowTime(UUID id, ShowTimeRequestDTO dto);

    void deleteShowTime(UUID id);

    List<ShowTimeResponseDTO> getShowTimesByCinemaAndMovie(UUID cinemaId, UUID movieId);

    List<ShowTimeResponseDTO> getShowTimesByCinemaAndMovieAndShowDate(UUID cinemaId, UUID movieId, LocalDate showDate);

    String getProjectionTypeByShowTimeId(UUID id);
}
