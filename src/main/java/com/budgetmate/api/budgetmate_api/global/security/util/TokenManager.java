package com.budgetmate.api.budgetmate_api.global.security.util;

import com.budgetmate.api.budgetmate_api.global.security.Token;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenManager {

    private final TokenProvider tokenProvider;

    public void validateToken(String token) {
        tokenProvider.validateToken(token);
    }

    public Token issueTokens(HttpServletResponse response, String username, Long userId) {
        // JWT 생성
        String accessToken = tokenProvider.createJwt("access", username, userId);
        String refreshToken = tokenProvider.createJwt("refresh", username, userId);

        // Redis에 Refresh Token 저장


        // 응답에 토큰 추가
        response.addHeader("Authorization", "Bearer " + accessToken);
        response.addCookie(createCookie("refresh", refreshToken));

        response.setStatus(HttpStatus.OK.value());

        return new Token(accessToken,refreshToken);
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
    public Long getUserId(String token){
        return tokenProvider.getUserId(token);
    }


}
