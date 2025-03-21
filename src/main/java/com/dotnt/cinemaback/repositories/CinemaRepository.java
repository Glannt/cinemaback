package com.dotnt.cinemaback.repositories;

import com.dotnt.cinemaback.dto.CinemaDTO;
import com.dotnt.cinemaback.models.Cinema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CinemaRepository extends JpaRepository<Cinema, UUID> {

    Optional<Cinema> findCinemaById(UUID cinemaId);

    CinemaDTO findCinemasByStatus(String status);
}
