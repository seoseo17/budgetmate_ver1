package com.budgetmate.api.budgetmate_api.global.security.util;

import static com.budgetmate.api.budgetmate_api.global.error.ErrorCode.INVALID_TOKEN;

import com.budgetmate.api.budgetmate_api.global.error.ErrorCode;
import com.budgetmate.api.budgetmate_api.global.exception.CustomException;
import com.budgetmate.api.budgetmate_api.global.exception.JwtAuthenticationException;
import com.budgetmate.api.budgetmate_api.global.security.RefreshTokenService;
import com.budgetmate.api.budgetmate_api.global.security.dto.Token;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.time.Instant;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenManager {

    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;

    public void validateToken(String token) {
        tokenProvider.validateToken(token);
        if(refreshTokenService.isBlackListToken(token)){
            throw new JwtAuthenticationException(INVALID_TOKEN);
        }
    }

    public Token issueTokens(HttpServletResponse response, String username, Long userId) {
        // JWT 생성
        String accessToken = tokenProvider.createJwt("access", username, userId);
        String refreshToken = tokenProvider.createJwt("refresh", username, userId);

        // Redis에 Refresh Token 저장
        saveRefreshToken(username,refreshToken);


        // 응답에 토큰 추가
        response.addHeader("Authorization", "Bearer " + accessToken);
        response.addCookie(createCookie("refresh", refreshToken));

        response.setStatus(HttpStatus.OK.value());

        return new Token(accessToken, refreshToken);
    }

    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24 * 60 * 60); // 하루 동안 유효
        cookie.setHttpOnly(true);
        return cookie;
    }

    public boolean isAccessToken(String accessToken) {
        return tokenProvider.getCategory(accessToken).equals("access");
    }

    public boolean isRefreshToken(String refreshToken) {
        return tokenProvider.getCategory(refreshToken).equals("refresh");
    }

    public String getUsername(String token) {
        return tokenProvider.getUsername(token);
    }

    public Long getUserId(String token) {
        return tokenProvider.getUserId(token);
    }

    private void saveRefreshToken(String username, String refreshToken) {
        try {
            refreshTokenService.saveRefreshToken(username, refreshToken); // Redis에 저장
        } catch (Exception e) {
            throw new CustomException(ErrorCode.REDIS_SERVER_ERROR);


        }
    }
    public void deleteRefreshToken(String refreshToken) {
        try {
            refreshTokenService.deleteRefreshToken(refreshToken); // Redis에 삭제
        } catch (Exception e) {
            throw new CustomException(ErrorCode.REDIS_SERVER_ERROR);
        }
    }

    public void addBlackList(String accessToken){

        tokenProvider.validateToken(accessToken);

        long expiredIn = tokenProvider.getExpiration(accessToken).toInstant().getEpochSecond();
        long validTime = expiredIn - Instant.now().getEpochSecond();

        log.info("[{}] AccessToken의 남은 만료 시간은 {}초 입니다.", tokenProvider.getUsername(accessToken), validTime);
        refreshTokenService.addBlackList(validTime,accessToken);

    }

    public void validateRefreshToken(String refreshToken) {
        String username = tokenProvider.getUsername(refreshToken);
        // Redis에 저장된 Refresh Token과 비교
        String storedRefreshToken = refreshTokenService.getRefreshToken(username);
        if (!refreshToken.equals(storedRefreshToken)) {
            throw new CustomException(INVALID_TOKEN);
        }
    }
}