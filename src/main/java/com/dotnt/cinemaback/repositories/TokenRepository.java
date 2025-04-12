package com.dotnt.cinemaback.repositories;


import com.dotnt.cinemaback.models.Token;
import com.dotnt.cinemaback.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TokenRepository extends JpaRepository<Token, UUID> {
    List<Token> findByUser(User user);
    Token findByToken(String token);
    Token findByRefreshToken(String token);
}

