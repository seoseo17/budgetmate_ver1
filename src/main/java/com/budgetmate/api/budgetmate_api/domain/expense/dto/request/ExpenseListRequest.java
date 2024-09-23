package com.budgetmate.api.budgetmate_api.domain.expense.dto.request;

import java.time.LocalDate;

public class ExpenseListRequest {

    private LocalDate start;
    private LocalDate end;

    private String category;

    private int min;

    private int max;

}
