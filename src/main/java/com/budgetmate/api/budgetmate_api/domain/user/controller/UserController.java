package com.budgetmate.api.budgetmate_api.domain.user.controller;

import com.budgetmate.api.budgetmate_api.domain.user.dto.signup.UserRequestDto;
import com.budgetmate.api.budgetmate_api.domain.user.entity.User;
import com.budgetmate.api.budgetmate_api.domain.user.service.UserService;
import com.budgetmate.api.budgetmate_api.global.CommonResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/user")
@RestController
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody @Valid UserRequestDto dto){
        userService.signup(dto);

        return new ResponseEntity<>(CommonResponse.ok("회원가입이 되었습니다.",null), HttpStatus.CREATED);
    }
}
