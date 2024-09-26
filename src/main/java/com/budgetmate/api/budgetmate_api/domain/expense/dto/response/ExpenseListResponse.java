package com.budgetmate.api.budgetmate_api.domain.expense.dto.response;

import com.budgetmate.api.budgetmate_api.domain.expense.dto.ExpenseDto;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpenseListResponse {

    private List<ExpenseDto> expenseList;
    private long totalExpense;

    private Map<String , Long> totalExpenseByCategory;
}
