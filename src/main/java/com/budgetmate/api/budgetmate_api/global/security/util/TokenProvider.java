package com.budgetmate.api.budgetmate_api.global.security.util;


import static io.jsonwebtoken.SignatureAlgorithm.HS256;
import static org.springframework.security.config.Elements.JWT;

import com.budgetmate.api.budgetmate_api.global.error.ErrorCode;
import com.budgetmate.api.budgetmate_api.global.exception.JwtAuthenticationException;
import com.budgetmate.api.budgetmate_api.global.security.Token;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Jwts;

@Slf4j
@Component
public class TokenProvider {

    private final SecretKey secretKey;
    private final Long accessExpiration;
    private final Long refreshExpiration;

    public TokenProvider(@Value("${jwt.secret}") String secret,
        @Value("${jwt.access-token-validate-in-seconds}") String accessExpiration,
        @Value("${jwt.refresh-token-validate-in-seconds}") String refreshExpiration) {
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        this.accessExpiration = Long.parseLong(accessExpiration) * 1000;
        this.refreshExpiration = Long.parseLong(refreshExpiration) * 1000;
    }

    public void validateToken(String token) {
        try {
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token); // JWT 서명 및 유효성 검증
        } catch (ExpiredJwtException e) {
            throw new JwtAuthenticationException(ErrorCode.TOKEN_EXPIRED, e);
        } catch (MalformedJwtException | UnsupportedJwtException |
                 SignatureException | IllegalArgumentException e) {
            throw new JwtAuthenticationException(ErrorCode.INVALID_TOKEN, e);
        }
    }

    public String getUsername(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload()
            .get("email", String.class);
    }

    public String getCategory(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload()
            .get("category", String.class);
    }

    public Long getUserId(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload()
            .get("userId", Long.class);
    }

    public String createJwt(String category, String userEmail, Long userId) {
        return Jwts.builder()
            .claim("category", category)
            .claim("email", userEmail)
            .claim("userId", userId)
            .issuedAt(new Date(System.currentTimeMillis()))
            .expiration(new Date(
                System.currentTimeMillis() + (category.equals("refresh") ? refreshExpiration
                    : accessExpiration)))
            .signWith(secretKey)
            .compact();
    }

}
