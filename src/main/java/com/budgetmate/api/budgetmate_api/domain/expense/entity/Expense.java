package com.budgetmate.api.budgetmate_api.domain.expense.entity;

import com.budgetmate.api.budgetmate_api.domain.user.entity.User;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "expense")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private Long categoryId;

    private LocalDate expenseAt;

    private long amount;

    private String memo;

    private boolean excludedSum = false;
    public void updateExpense(LocalDate expenseDate, String memo, long amount, Long categoryId, boolean excludedSum) {
        this.expenseAt = expenseDate;
        this.memo = memo;
        this.amount = amount;
        this.categoryId = categoryId;
        this.excludedSum = excludedSum;
    }
}
