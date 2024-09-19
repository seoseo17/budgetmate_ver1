package com.budgetmate.api.budgetmate_api.global.security;

import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    public static final String REDIS_BLACK_LIST_PREFIX = "BlackList:";
    public static final String BLACK_LIST_DEFAULT_VALUE = "Logout";

    private final RedisTemplate<String,Object> redisTemplate;


    @Value("${jwt.refresh-token-validate-in-seconds}")
    private long refreshTokenExpiration;

    public void saveRefreshToken(String username, String refreshToken) {
        redisTemplate.opsForValue().set(
            getRefreshTokenKey(username),  // Redis에 저장할 키
            refreshToken,                  // 저장할 Refresh Token 값
            refreshTokenExpiration,        // 만료 시간
            TimeUnit.MILLISECONDS          // 시간 단위
        );
    }

    public String getRefreshToken(String username) {
        return (String) redisTemplate.opsForValue().get(getRefreshTokenKey(username));
    }

    public void deleteRefreshToken(String username) {
        redisTemplate.delete(getRefreshTokenKey(username));
    }

    public void addBlackList(Long validTime,String accessToken){
        if (validTime > 0){
            redisTemplate.opsForValue().set(
                REDIS_BLACK_LIST_PREFIX+accessToken,
                BLACK_LIST_DEFAULT_VALUE,
                validTime
            );
        }
    }

    public boolean isBlackListToken(String token) {
        String blackListToken = (String) redisTemplate.opsForValue().get(
            REDIS_BLACK_LIST_PREFIX + token);
        return blackListToken != null;
    }

    private String getRefreshTokenKey(String username) {
        return "refreshToken:" + username;
    }
}
