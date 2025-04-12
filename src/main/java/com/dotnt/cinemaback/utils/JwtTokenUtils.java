package com.dotnt.cinemaback.utils;

import com.dotnt.cinemaback.constants.enums.UserStatus;
import com.dotnt.cinemaback.models.Token;
import com.dotnt.cinemaback.models.User;
import com.dotnt.cinemaback.repositories.TokenRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.InvalidParameterException;
import java.security.SecureRandom;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
@Slf4j(topic = "JwtTokenUtils")
public class JwtTokenUtils {
    @Value("${jwt.expiration}")
    private int expiration; //save to an environment variable

    @Value("${jwt.expiration-refresh-token}")
    private int expirationRefreshToken;

    @Value("${jwt.secretKey}")
    private String secretKey;
    private final TokenRepository tokenRepository;
    public String generateToken(User user) throws Exception{
        Map<String, Object> claims = new HashMap<>();
        String subject = getSubject(user);
        claims.put("subject", subject);
        claims.put("userId", user.getId());

        try{
            String token = Jwts.builder()
                    .claims(claims) //how to extract claims from this ?
                    .subject(subject)
                    .expiration(new Date(System.currentTimeMillis() + expiration * 1000L))
                    .signWith(getSignInKey(), Jwts.SIG.HS256)
                    .compact();
            return token;
        }catch(Exception e){
            throw new InvalidParameterException("Cannot create jwt token, error: "+e.getMessage());
        }

    }

    private static String getSubject(User user){
        return user.getEmail();
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = secretKey.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private String generateSecretKey(){
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[32]; // 256 bits
        random.nextBytes(key);
        return Encoders.BASE64.encode(key);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()  // Khởi tạo JwtParserBuilder
                .verifyWith(getSignInKey())  // Sử dụng verifyWith() để thiết lập signing key
                .build()  // Xây dựng JwtParser
                .parseSignedClaims(token)  // Phân tích token đã ký
                .getPayload();  // Lấy phần body của JWT, chứa claims
    }

    public  <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = this.extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    //check expiration
    public boolean isTokenExpired(String token) {
        Date expirationDate = this.extractClaim(token, Claims::getExpiration);
        return expirationDate.before(new Date());
    }
    public String getSubject(String token) {
        return  extractClaim(token, Claims::getSubject);
    }
    public boolean validateToken(String token, User userDetails) {
        try {
            String subject = extractClaim(token, Claims::getSubject);
            //subject is phoneNumber or email
            Token existingToken = tokenRepository.findByToken(token);
            if(existingToken == null ||
                    existingToken.isRevoked() == true ||
                    userDetails.getStatus() == UserStatus.INACTIVE
            ) {
                return false;
            }
            return (subject.equals(userDetails.getUsername()))
                    && !isTokenExpired(token);
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }
}
