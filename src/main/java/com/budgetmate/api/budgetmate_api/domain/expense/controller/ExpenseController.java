package com.budgetmate.api.budgetmate_api.domain.expense.controller;

import com.budgetmate.api.budgetmate_api.domain.category.service.CategoryService;
import com.budgetmate.api.budgetmate_api.domain.expense.dto.ExpenseDto;
import com.budgetmate.api.budgetmate_api.domain.expense.dto.request.ExpenseCreateRequest;
import com.budgetmate.api.budgetmate_api.domain.expense.service.ExpenseService;
import com.budgetmate.api.budgetmate_api.global.CommonResponse;
import com.budgetmate.api.budgetmate_api.global.security.userDetails.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

        return new ResponseEntity<>(CommonResponse.ok("지출 생성 되었습니다.",null),HttpStatus.CREATED);
    }

    @PutMapping("/{expendId}")
    public ResponseEntity<?> updateExpense(@AuthenticationPrincipal CustomUserDetails user,
        @PathVariable Long expendId,
        @RequestBody @Valid ExpenseCreateRequest dto){
        categoryService.findById(dto.getCategoryId());
        expenseService.updateExpense(user.getUserId(),expendId,dto);

        return new ResponseEntity<>(CommonResponse.ok("지출 수정 되었습니다.",null),HttpStatus.OK);
    }

    @DeleteMapping("/{expendId}")
    public ResponseEntity<?> deleteExpense(@AuthenticationPrincipal CustomUserDetails user,
        @PathVariable Long expendId){
        expenseService.deleteExpense(user.getUserId(), expendId);

        return new ResponseEntity<>(CommonResponse.ok("지출 삭제 되었습니다.",null),HttpStatus.OK);
    }

    @GetMapping("/{expendId}")
    public ResponseEntity<?> getExpenseDetail(@AuthenticationPrincipal CustomUserDetails user,
        @PathVariable Long expendId
         ){
        ExpenseDto dto = expenseService.getExpenseDetail(user.getUserId(), expendId);
        return new ResponseEntity(CommonResponse.ok("조회 되었습니다.",dto),HttpStatus.OK);
    }
}
