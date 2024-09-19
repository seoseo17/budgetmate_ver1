package com.budgetmate.api.budgetmate_api.global.security.filter;

import static com.budgetmate.api.budgetmate_api.global.error.ErrorCode.INVALID_INPUT_VALUE;


import com.budgetmate.api.budgetmate_api.global.CommonResponse;
import com.budgetmate.api.budgetmate_api.global.error.ErrorCode;
import com.budgetmate.api.budgetmate_api.global.error.ErrorResponse;
import com.budgetmate.api.budgetmate_api.global.exception.JwtAuthenticationException;
import com.budgetmate.api.budgetmate_api.global.security.userDetails.CustomUserDetails;
import com.budgetmate.api.budgetmate_api.global.security.dto.Token;
import com.budgetmate.api.budgetmate_api.global.security.dto.LoginRequestDto;
import com.budgetmate.api.budgetmate_api.global.security.util.TokenManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Slf4j
public class LoginFilter extends AbstractAuthenticationProcessingFilter {

    private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER =
        new AntPathRequestMatcher("/user/login", "POST");

    private final ObjectMapper objectMapper;
    private final AuthenticationManager authenticationManager;
    private final TokenManager tokenManager;


    public LoginFilter(ObjectMapper objectMapper, AuthenticationManager authenticationManager,TokenManager tokenManager) {
        super(DEFAULT_ANT_PATH_REQUEST_MATCHER);
        this.objectMapper = objectMapper;
        this.authenticationManager = authenticationManager;
        this.tokenManager = tokenManager;
    }
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
        HttpServletResponse response)
        throws AuthenticationException, IOException, ServletException {
        LoginRequestDto loginRequest = getLoginInfo(request);
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();
        log.info("Login email = {} / password = {}", email, password);

        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(email,password);
        return authenticationManager.authenticate(authRequest);
    }


    @Override
    protected void successfulAuthentication(HttpServletRequest request,
        HttpServletResponse response, FilterChain chain, Authentication authResult)throws IOException {

        CustomUserDetails customUserDetails = (CustomUserDetails) authResult.getPrincipal();
        String username = customUserDetails.getUsername();
        Long userId = customUserDetails.getUserId();
        Token token = tokenManager.issueTokens(response, username, userId);

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.getWriter().write(
            objectMapper.writeValueAsString(CommonResponse.ok("로그인 되었습니다.",token)));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
        HttpServletResponse response, AuthenticationException failed) throws IOException {
        log.error("사용자 로그인 인증 실패: {}", failed.getMessage());
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.getWriter().write(
            objectMapper.writeValueAsString(new ErrorResponse(ErrorCode.AUTHENTICATION_FAILED)));
    }

    private LoginRequestDto getLoginInfo(HttpServletRequest request)
        throws JwtAuthenticationException {
        try {
            return objectMapper.readValue(request.getReader(), LoginRequestDto.class);
        } catch (IOException e) {
            log.error("[ERROR] Login request Dto: {}",e.getMessage());
            throw new JwtAuthenticationException(INVALID_INPUT_VALUE);
        }
    }
}
