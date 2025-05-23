package com.dotnt.cinemaback.repositories;

import com.dotnt.cinemaback.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findAllByEmail(String email);
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}
