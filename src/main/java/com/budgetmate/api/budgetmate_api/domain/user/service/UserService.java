package com.budgetmate.api.budgetmate_api.domain.user.service;

import static com.budgetmate.api.budgetmate_api.global.error.ErrorCode.EMAIL_ALREADY_EXISTS;
import static com.budgetmate.api.budgetmate_api.global.error.ErrorCode.USER_NOT_FOUND;

import com.budgetmate.api.budgetmate_api.domain.user.dto.signup.UserRequestDto;
import com.budgetmate.api.budgetmate_api.domain.user.entity.User;
import com.budgetmate.api.budgetmate_api.domain.user.repository.UserRepository;
import com.budgetmate.api.budgetmate_api.global.error.ErrorCode;
import com.budgetmate.api.budgetmate_api.global.exception.CustomException;
import com.budgetmate.api.budgetmate_api.global.security.dto.Token;
import com.budgetmate.api.budgetmate_api.global.security.util.TokenManager;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenManager tokenManager;

    public void signup(UserRequestDto dto){

        if (userRepository.existsByEmail(dto.getEmail())){
            throw new CustomException(EMAIL_ALREADY_EXISTS);
        }

        String encodedPassword = passwordEncoder.encode(dto.getPassword());

        User user = User.builder()
            .email(dto.getEmail())
            .nickname(dto.getNickname())
            .password(encodedPassword)
            .build();
        userRepository.save(user);
        log.info("[sign up] user_id:{}",user.getId());
    }

    public User findById(Long id){
        return userRepository.findById(id).orElseThrow(()-> new CustomException(USER_NOT_FOUND));
    }

    public Token reissue(HttpServletRequest request, HttpServletResponse response) {
        //get refresh token
        Cookie[] cookies = request.getCookies();
        String refreshToken = Arrays.stream(cookies).filter(cookie -> cookie.getName().equals("refresh")).findFirst().orElseThrow(() -> new CustomException(ErrorCode.REFRESH_TOKEN_NOT_FOUND)).getValue();

        tokenManager.validateToken(refreshToken);

        // 토큰이 refresh인지 확인 (발급시 페이로드에 명시)
        if (!tokenManager.isRefreshToken(refreshToken)) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }

        tokenManager.validateRefreshToken(refreshToken); //redis에 저장된 refreshToken 과 비교
        String username = tokenManager.getUsername(refreshToken);
        Long userId = tokenManager.getUserId(refreshToken);
        tokenManager.deleteRefreshToken(refreshToken);
        return tokenManager.issueTokens(response, username, userId);
    }
}
