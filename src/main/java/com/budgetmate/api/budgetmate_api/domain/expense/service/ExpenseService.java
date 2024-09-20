package com.budgetmate.api.budgetmate_api.domain.expense.service;

import com.budgetmate.api.budgetmate_api.domain.expense.dto.ExpenseCreateRequest;
import com.budgetmate.api.budgetmate_api.domain.expense.entity.Expense;
import com.budgetmate.api.budgetmate_api.domain.expense.repository.ExpenseRepository;
import com.budgetmate.api.budgetmate_api.domain.user.entity.User;
import com.budgetmate.api.budgetmate_api.domain.user.service.UserService;
import java.time.LocalDate;
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
        int amount = dto.getAmount();
        LocalDate expenseDate = dto.getExpenseDate();
        String memo = dto.getMemo();
        long categoryId = dto.getCategoryId();

        Expense expense = Expense.builder()
            .user(user)
            .categoryId(categoryId)
            .expenseAt(expenseDate)
            .amount(amount)
            .memo(memo)
            .build();
        expenseRepository.save(expense);
    }
}
