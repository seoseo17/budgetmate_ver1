package com.budgetmate.api.budgetmate_api.domain.category.controller;

import com.budgetmate.api.budgetmate_api.domain.category.service.CategoryService;
import com.budgetmate.api.budgetmate_api.global.CommonResponse;
import com.budgetmate.api.budgetmate_api.global.security.userDetails.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/list")
    public ResponseEntity<?> getCategoryList(@AuthenticationPrincipal CustomUserDetails userDetails){

        CommonResponse commonResponse = new CommonResponse<>(null,categoryService.getCategoryList());

        return new ResponseEntity<>(commonResponse,HttpStatus.OK);
    }

}
