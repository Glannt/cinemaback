package com.dotnt.cinemaback.services.token;

import com.dotnt.cinemaback.exception.DataNotFoundException;
import com.dotnt.cinemaback.exception.ExpiredTokenException;
import com.dotnt.cinemaback.models.Token;
import com.dotnt.cinemaback.models.User;
import com.dotnt.cinemaback.repositories.TokenRepository;
import com.dotnt.cinemaback.utils.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TokenService implements ITokenService{

    private static final int MAX_TOKENS = 3;
    @Value("${jwt.expiration}")
    private int expiration; //save to an environment variable

    @Value("${jwt.expiration-refresh-token}")
    private int expirationRefreshToken;
    private final TokenRepository tokenRepository;
    private final JwtTokenUtils jwtTokenUtils;

    @Override
    @Transactional
    public Token addToken(User user, String token, boolean isMobileDevice) {
        List<Token> tokens =tokenRepository.findByUser(user);
        int tokenCount = tokens.size();
        if(tokenCount >= MAX_TOKENS) {
            boolean hasNonMobileToken = !tokens.stream().allMatch(Token::isMobile);
            Token tokenDelete;
            if(hasNonMobileToken) {
                tokenDelete = tokens
                        .stream()
                        .filter(t -> !t.isMobile())
                        .findFirst()
                        .orElse(null);
            } else {
                tokenDelete = tokens.get(0);
            }

            tokenRepository.delete(tokenDelete);
        }
        long expirationInSeconds = expiration;
        LocalDateTime expirationDateTime = LocalDateTime.now().plusSeconds(expirationInSeconds);
        // Tạo mới một token cho người dùng
        Token newToken = Token.builder()
                .user(user)
                .token(token)
                .revoked(false)
                .expired(false)
                .tokenType("Bearer")
                .expirationDate(expirationDateTime)
                .isMobile(isMobileDevice)
                .build();

        newToken.setRefreshToken(UUID.randomUUID().toString());
        newToken.setRefreshExpirationDate(LocalDateTime.now().plusSeconds(expirationRefreshToken));
        tokenRepository.save(newToken);
        return newToken;
    }

    @Override
    @Transactional
    public Token refreshToken(String refreshToken, User user) throws Exception {
        Token existingToken = tokenRepository.findByRefreshToken(refreshToken);
        if(existingToken == null) {
            throw new DataNotFoundException("Refresh token does not exist");
        }
        if(existingToken.getRefreshExpirationDate().compareTo(LocalDateTime.now()) < 0){
            tokenRepository.delete(existingToken);
            throw new ExpiredTokenException("Refresh token is expired");
        }
        String token = jwtTokenUtils.generateToken(user);
        LocalDateTime expirationDateTime = LocalDateTime.now().plusSeconds(expiration);
        existingToken.setExpirationDate(expirationDateTime);
        existingToken.setToken(token);
        existingToken.setRefreshToken(UUID.randomUUID().toString());
        existingToken.setRefreshExpirationDate(LocalDateTime.now().plusSeconds(expirationRefreshToken));
        return existingToken;
    }
}
