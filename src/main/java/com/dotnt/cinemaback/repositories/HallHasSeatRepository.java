package com.dotnt.cinemaback.repositories;


import com.dotnt.cinemaback.models.HallHasSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface HallHasSeatRepository extends JpaRepository<HallHasSeat, UUID> {
}