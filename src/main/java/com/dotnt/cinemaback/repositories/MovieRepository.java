package com.dotnt.cinemaback.repositories;

import com.dotnt.cinemaback.models.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MovieRepository extends JpaRepository<Movie, UUID> {
    List<Movie> findByMovieGenres_Genre_NameIgnoreCase(String genreName);
    Page<Movie> findByStatusNot(String status, Pageable pageable);

    List<Movie> findByStatusIgnoreCase(String status);

}
