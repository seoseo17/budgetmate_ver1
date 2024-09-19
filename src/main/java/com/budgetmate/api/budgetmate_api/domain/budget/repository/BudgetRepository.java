package com.budgetmate.api.budgetmate_api.domain.budget.repository;

import com.budgetmate.api.budgetmate_api.domain.budget.entity.Budget;
import com.budgetmate.api.budgetmate_api.domain.categorybudget.entity.CategoryBudget;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BudgetRepository extends JpaRepository<Budget,Long> {


    Budget findByUserIdAndYearAndAndMonth(Long userId, int year, int month);

    boolean existsByYearAndMonth(int year, int month);

}
