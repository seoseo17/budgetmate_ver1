package com.budgetmate.api.budgetmate_api.domain.category.controller;

import com.budgetmate.api.budgetmate_api.domain.category.service.CategoryService;
import com.budgetmate.api.budgetmate_api.global.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/list")
    public ResponseEntity<?> getCategoryList(){
        CommonResponse commonResponse = new CommonResponse<>(null,categoryService.getCategoryList());

        return new ResponseEntity<>(commonResponse,HttpStatus.OK);
    }

}
