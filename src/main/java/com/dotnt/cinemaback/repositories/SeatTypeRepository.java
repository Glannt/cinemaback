package com.dotnt.cinemaback.repositories;

import com.dotnt.cinemaback.constants.enums.ESeatType;
import com.dotnt.cinemaback.models.SeatType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SeatTypeRepository extends JpaRepository<SeatType, UUID> {
    Optional<SeatType> findByName(ESeatType name);
}
