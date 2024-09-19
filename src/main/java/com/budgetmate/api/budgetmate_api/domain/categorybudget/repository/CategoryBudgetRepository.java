package com.budgetmate.api.budgetmate_api.domain.categorybudget.repository;

import com.budgetmate.api.budgetmate_api.domain.categorybudget.entity.CategoryBudget;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryBudgetRepository extends JpaRepository<CategoryBudget,Long> {

    boolean existsByCategory_IdAndBudget_Id(Long categoryId, Long budgetId);

    CategoryBudget findByCategory_IdAndBudget_Id(Long categoryId, Long budgetId);

    List<CategoryBudget> findAllByBudget_Id(Long budgetId);

}
