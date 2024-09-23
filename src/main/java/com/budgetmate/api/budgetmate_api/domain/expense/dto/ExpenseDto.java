package com.budgetmate.api.budgetmate_api.domain.expense.dto;

import com.budgetmate.api.budgetmate_api.domain.expense.entity.Expense;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpenseDto {

    private int amount;

    private Long categoryId;

    private String memo;

    private LocalDate expenseDate;

    private boolean excludedSum;

    public ExpenseDto fromEntity(Expense expense){
        return ExpenseDto.builder()
            .amount(expense.getAmount())
            .categoryId(expense.getCategoryId())
            .memo(expense.getMemo())
            .expenseDate(expense.getExpenseAt())
            .excludedSum(expense.isExcludedSum())
            .build();
    }
}
