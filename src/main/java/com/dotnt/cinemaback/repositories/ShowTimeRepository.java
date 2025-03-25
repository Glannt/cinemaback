package com.dotnt.cinemaback.repositories;

import com.dotnt.cinemaback.models.ShowTime;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ShowTimeRepository extends JpaRepository<ShowTime, UUID> {
    List<ShowTime> findByMovie_Id(UUID movieId);

    List<ShowTime> findByHall_Cinema_Id(UUID cinemaId);

    List<ShowTime> findByEndTimeBefore(LocalDateTime endTime);

    List<ShowTime> findByMovieId(UUID movieId);

    List<ShowTime> findByHall_Cinema_IdAndMovie_Id(UUID cinemaId, UUID movieId);

    List<ShowTime> findByHall_Cinema_IdAndMovie_IdAndShowDate(UUID cinemaId, UUID movieId, LocalDate showDate);
}
