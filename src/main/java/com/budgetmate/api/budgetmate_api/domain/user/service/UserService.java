package com.budgetmate.api.budgetmate_api.domain.user.service;

import com.budgetmate.api.budgetmate_api.domain.user.dto.signup.UserRequestDto;
import com.budgetmate.api.budgetmate_api.domain.user.entity.User;
import com.budgetmate.api.budgetmate_api.domain.user.repository.UserRepository;
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

        String encodedPassword = passwordEncoder.encode(dto.getPassword());

        User user = User.builder()
            .email(dto.getEmail())
            .nickname(dto.getNickname())
            .password(encodedPassword)
            .build();
        userRepository.save(user);
    }
}
