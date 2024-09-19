package com.budgetmate.api.budgetmate_api.domain.categorybudget.controller;

import com.budgetmate.api.budgetmate_api.domain.category.entity.Category;
import com.budgetmate.api.budgetmate_api.domain.category.service.CategoryService;
import com.budgetmate.api.budgetmate_api.domain.categorybudget.dto.CategoryBudgetCreateReqeust;
import com.budgetmate.api.budgetmate_api.domain.categorybudget.dto.CategoryBudgetUpdateReqeust;
import com.budgetmate.api.budgetmate_api.domain.categorybudget.service.CategoryBudgetService;
import com.budgetmate.api.budgetmate_api.global.security.userDetails.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/category-budget")
@RequiredArgsConstructor
public class CategoryBudgetController {

    private final CategoryBudgetService categoryBudgetService;
    private final CategoryService categoryService;

    @RequestMapping()
    public ResponseEntity<?> createBudgetByCategory(@AuthenticationPrincipal CustomUserDetails user,
        @Valid @RequestBody CategoryBudgetCreateReqeust dto){


        Category category = categoryService.findById(dto.getCategoryId());
        categoryBudgetService.createBudgetByCategory(user.getUserId(),category,dto);

        return new ResponseEntity<>(HttpStatus.CREATED);

    }
    @PutMapping("/{categoryBudgetId}")
    public ResponseEntity<?> updateBudgetByCategory(@AuthenticationPrincipal CustomUserDetails user,
        @PathVariable Long categoryBudgetId,
        @Valid @RequestBody CategoryBudgetUpdateReqeust dto){

        Category category = categoryService.findById(dto.getCategoryId());
        categoryBudgetService.updateBudgetByCategory(user.getUserId(),categoryBudgetId,category,dto);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
