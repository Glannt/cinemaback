package com.dotnt.cinemaback.repositories;

import com.dotnt.cinemaback.models.ShowTime;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ShowTimeRepository extends JpaRepository<ShowTime, UUID> {
}
