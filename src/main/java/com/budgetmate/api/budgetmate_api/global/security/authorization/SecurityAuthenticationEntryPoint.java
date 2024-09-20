package com.budgetmate.api.budgetmate_api.global.security.authorization;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.budgetmate.api.budgetmate_api.global.CommonResponse;
import com.budgetmate.api.budgetmate_api.global.error.ErrorCode;
import com.budgetmate.api.budgetmate_api.global.error.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
@Slf4j
@RequiredArgsConstructor
public class SecurityAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper;

    @Override
    public void commence(
        HttpServletRequest request,
        HttpServletResponse response,
        AuthenticationException authException
    ) throws IOException {
        String exception = (String)request.getAttribute("exception");

        // 예외가 없는 경우를 대비해 null 체크
        if (exception == null) {
            sendErrorResponse(response, ErrorCode.COMMON_SYSTEM_ERROR);
            return;
        }

        // 예외별 처리 로직
        if (exception.equals(ErrorCode.INVALID_TOKEN.name())) {
            sendErrorResponse(response, ErrorCode.INVALID_TOKEN);
        } else if (exception.equals(ErrorCode.TOKEN_EXPIRED.name())) {
            sendErrorResponse(response, ErrorCode.TOKEN_EXPIRED);
        } else {
            sendErrorResponse(response, ErrorCode.ACCESS_DENIED);
        }
    }

    private void sendErrorResponse(HttpServletResponse response, ErrorCode errorCode)
        throws IOException {
        response.setStatus(UNAUTHORIZED.value());
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(UTF_8.name());
        objectMapper.writeValue(response.getWriter(), new ErrorResponse(errorCode, errorCode.getMessage()));
    }

}
