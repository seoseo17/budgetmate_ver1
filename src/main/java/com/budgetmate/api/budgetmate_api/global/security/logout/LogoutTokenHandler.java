package com.budgetmate.api.budgetmate_api.global.security.logout;

import static com.budgetmate.api.budgetmate_api.global.error.ErrorCode.INVALID_INPUT_VALUE;

import com.budgetmate.api.budgetmate_api.global.exception.JwtAuthenticationException;
import com.budgetmate.api.budgetmate_api.global.security.RefreshTokenService;
import com.budgetmate.api.budgetmate_api.global.security.dto.LoginRequestDto;
import com.budgetmate.api.budgetmate_api.global.security.dto.LogoutRequestDto;
import com.budgetmate.api.budgetmate_api.global.security.util.TokenManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
@Slf4j
@RequiredArgsConstructor
public class LogoutTokenHandler implements LogoutHandler {

    private final ObjectMapper objectMapper;
    private final TokenManager tokenManager;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) {
        LogoutRequestDto logoutDto = getLogoutInfo(request);

        tokenManager.validateToken(logoutDto.getAccessToken());
        tokenManager.validateToken(logoutDto.getRefreshToken());

        log.info("Logout Info = {}", logoutDto);
        tokenManager.deleteRefreshToken(logoutDto.getRefreshToken());
        tokenManager.addBlackList(logoutDto.getAccessToken());
    }

    private LogoutRequestDto getLogoutInfo(HttpServletRequest request)
        throws JwtAuthenticationException {
        try {
            return objectMapper.readValue(request.getReader(), LogoutRequestDto.class);
        } catch (IOException e) {
            log.error("[ERROR] Logout request Dto: {}",e.getMessage());
            throw new JwtAuthenticationException(INVALID_INPUT_VALUE);
        }
    }
}
