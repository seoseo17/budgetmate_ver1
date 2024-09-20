package com.budgetmate.api.budgetmate_api.domain.expense.controller;

import com.budgetmate.api.budgetmate_api.domain.category.service.CategoryService;
import com.budgetmate.api.budgetmate_api.domain.expense.dto.ExpenseCreateRequest;
import com.budgetmate.api.budgetmate_api.domain.expense.service.ExpenseService;
import com.budgetmate.api.budgetmate_api.global.security.userDetails.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/expenses")
@RequiredArgsConstructor
@Slf4j
public class ExpenseController {

    private final ExpenseService expenseService;
    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<?> createExpense(@AuthenticationPrincipal CustomUserDetails user,
        @RequestBody @Valid ExpenseCreateRequest dto){
        categoryService.findById(dto.getCategoryId());
        expenseService.createExpense(user.getUserId(),dto);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
