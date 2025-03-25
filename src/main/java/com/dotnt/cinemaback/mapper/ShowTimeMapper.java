package com.dotnt.cinemaback.mapper;

import com.dotnt.cinemaback.dto.request.ShowTimeRequestDTO;
import com.dotnt.cinemaback.dto.response.ShowTimeResponseDTO;
import com.dotnt.cinemaback.models.Hall;
import com.dotnt.cinemaback.models.Movie;
import com.dotnt.cinemaback.models.ShowTime;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ShowTimeMapper {
    public ShowTimeResponseDTO convertToResponseDTO(ShowTime showTime) {
        return ShowTimeResponseDTO.builder()
                .id(showTime.getId())
                .movieTitle(showTime.getMovie().getTitle())
                .hallName(showTime.getHall().getName())
                .startTime(showTime.getStartTime())
                .endTime(showTime.getEndTime())
                .price(showTime.getTicketPrice())
                .showDate(showTime.getShowDate())
                .projectionType(showTime.getHall().getProjectionType().toString())
                .build();
    }

    public List<ShowTimeResponseDTO> convertListToResponseDTO(List<ShowTime> showTimes) {
        return showTimes.stream().map(this::convertToResponseDTO).toList();
    }

    public ShowTime convertToEntity(ShowTimeRequestDTO dto, Movie movie, Hall hall) {
        return ShowTime.builder()
                .movie(movie)           // đã fetch từ DB bằng movieId
                .hall(hall)             // đã fetch từ DB bằng hallId
                .startTime(dto.getStartTime())
                .endTime(dto.getEndTime())
                .ticketPrice(dto.getPrice())
                .showDate(dto.getShowDate())
                .build();
    }
}
