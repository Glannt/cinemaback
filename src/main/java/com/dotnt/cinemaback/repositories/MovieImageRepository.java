package com.dotnt.cinemaback.repositories;

import com.dotnt.cinemaback.models.MovieImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MovieImageRepository extends JpaRepository<MovieImage, UUID> {

}
