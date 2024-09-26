package com.budgetmate.api.budgetmate_api.domain.expense.repository;

import com.budgetmate.api.budgetmate_api.domain.expense.entity.Expense;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense,Long> {

    Optional<Expense> findByIdAndUser_Id(Long id, Long userId);

    @Query("select e from Expense e where e.user.id = :userId "
        + "and e.expenseAt>= :strDate "
        + "and e.expenseAt <= :endDate "
        + "and e.amount >= :minAmount "
        + "and e.amount <= :maxAmount")
    List<Expense> findExpensesByUserAndDateAndPrice(
        @Param("userId") long userId,
        @Param("strDate")LocalDate strDate,
        @Param("endDate")LocalDate endDate,
        @Param("minAmount")long minAmount,
        @Param("maxAmount")long maxAmount
    );
    @Query("select e from Expense e where e.user.id = :userId "
        + "and e.categoryId = :categoryId "
        + "and e.expenseAt>= :strDate "
        + "and e.expenseAt <= :endDate "
        + "and e.amount >= :minAmount "
        + "and e.amount <= :maxAmount")
    List<Expense> findExpensesByCategoryAndUserAndDateAndPrice(
        @Param("userId") long userId,
        @Param("categoryId") long categoryId,
        @Param("strDate")LocalDate strDate,
        @Param("endDate")LocalDate endDate,
        @Param("minAmount")long minAmount,
        @Param("maxAmount")long maxAmount
    );
}
