package com.budgetmate.api.budgetmate_api.domain.user.service;

import static com.budgetmate.api.budgetmate_api.global.error.ErrorCode.EMAIL_ALREADY_EXISTS;

import com.budgetmate.api.budgetmate_api.domain.user.dto.signup.UserRequestDto;
import com.budgetmate.api.budgetmate_api.domain.user.entity.User;
import com.budgetmate.api.budgetmate_api.domain.user.repository.UserRepository;
import com.budgetmate.api.budgetmate_api.global.exception.CustomException;
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
}
