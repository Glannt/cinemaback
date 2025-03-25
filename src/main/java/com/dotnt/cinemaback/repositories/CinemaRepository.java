package com.dotnt.cinemaback.repositories;

import com.dotnt.cinemaback.constants.enums.CinemaStatus;
import com.dotnt.cinemaback.dto.CinemaDTO;
import com.dotnt.cinemaback.models.Cinema;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CinemaRepository extends JpaRepository<Cinema, UUID> {

    Optional<Cinema> findCinemaById(UUID cinemaId);

    CinemaDTO findCinemasByStatus(String status);

    @Query("SELECT c FROM Cinema c WHERE c.status <> '1'")
    Page<Cinema> findActiveCinemas(Pageable pageable);

    List<Cinema> findByStatus(CinemaStatus status);

    @Query(value = "SELECT DISTINCT c.* FROM cinema c " +
            "JOIN hall h ON h.cinema_id = c.id " +
            "JOIN show_time st ON st.hall_id = h.id " +
            "WHERE c.status = 'ACTIVE' AND st.movie_id = :movieId", nativeQuery = true)
    List<Cinema> findActiveCinemasByMovieIdNative(@Param("movieId") UUID movieId);

}
