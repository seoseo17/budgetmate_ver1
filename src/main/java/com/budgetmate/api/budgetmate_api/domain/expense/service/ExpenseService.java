package com.budgetmate.api.budgetmate_api.domain.expense.service;

import static com.budgetmate.api.budgetmate_api.global.error.ErrorCode.ACCESS_DENIED;
import static com.budgetmate.api.budgetmate_api.global.error.ErrorCode.EXPENSE_NOT_FOUND;

import com.budgetmate.api.budgetmate_api.domain.expense.dto.ExpenseDto;
import com.budgetmate.api.budgetmate_api.domain.expense.dto.request.ExpenseCreateRequest;
import com.budgetmate.api.budgetmate_api.domain.expense.entity.Expense;
import com.budgetmate.api.budgetmate_api.domain.expense.repository.ExpenseRepository;
import com.budgetmate.api.budgetmate_api.domain.user.entity.User;
import com.budgetmate.api.budgetmate_api.domain.user.service.UserService;
import com.budgetmate.api.budgetmate_api.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final UserService userService;

    public void createExpense(Long userId, ExpenseCreateRequest dto){
        User user = userService.findById(userId);

        Expense expense = createExpenseEntity(user,dto);
        expenseRepository.save(expense);
    }

    public void updateExpense(Long userId, Long expenseId, ExpenseCreateRequest dto){
        Expense expense = expenseRepository.findByIdAndUser_Id(expenseId, userId)
            .orElseThrow(() -> new CustomException(ACCESS_DENIED));

        expense.updateExpense(dto.getExpenseDate(),dto.getMemo(),dto.getAmount(), dto.getCategoryId(),dto.isExcludedSum());
        expenseRepository.save(expense);
    }

    public void deleteExpense(Long userId,Long expendId){
        Expense expense = expenseRepository.findByIdAndUser_Id(expendId, userId)
            .orElseThrow(() -> new CustomException(ACCESS_DENIED));
        expenseRepository.delete(expense);
    }

    public ExpenseDto getExpenseDetail(Long userId,Long expendId){
        Expense expense = findById(expendId);
        return new ExpenseDto().fromEntity(expense);

    }

    public Expense findById(Long expenseId){
        return expenseRepository.findById(expenseId).orElseThrow(
            () -> new CustomException(EXPENSE_NOT_FOUND));
    }

    private Expense createExpenseEntity(User user,ExpenseCreateRequest dto){
        return Expense.builder()
            .user(user)
            .categoryId(dto.getCategoryId())
            .expenseAt(dto.getExpenseDate())
            .amount(dto.getAmount())
            .memo(dto.getMemo())
            .excludedSum(dto.isExcludedSum())
            .build();
    }
}
