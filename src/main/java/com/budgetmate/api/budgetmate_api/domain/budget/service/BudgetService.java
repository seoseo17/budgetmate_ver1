package com.budgetmate.api.budgetmate_api.domain.budget.service;

import static com.budgetmate.api.budgetmate_api.global.error.ErrorCode.BUDGET_NOT_FOUND;

import com.budgetmate.api.budgetmate_api.domain.budget.entity.Budget;
import com.budgetmate.api.budgetmate_api.domain.budget.repository.BudgetRepository;
import com.budgetmate.api.budgetmate_api.domain.categorybudget.entity.CategoryBudget;
import com.budgetmate.api.budgetmate_api.domain.categorybudget.repository.CategoryBudgetRepository;
import com.budgetmate.api.budgetmate_api.global.exception.CustomException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final CategoryBudgetRepository categoryBudgetRepository;

    public Budget getBudgetByUserIdAndYearAndMonth(Long userId, int year,int month){
        return budgetRepository.findByUserIdAndYearAndAndMonth(userId,year,month);
    }
    public boolean checkBudgetExists(int year, int month){
        return budgetRepository.existsByYearAndMonth(year,month);
    }

    public Budget findById(Long budgetId){
        return budgetRepository.findById(budgetId).orElseThrow(()-> new CustomException(BUDGET_NOT_FOUND));
    }

    @Transactional
    public void recalculateTatolAmount(Long budgetId){
        Budget budget = findById(budgetId);
        List<CategoryBudget> categoryBudgets =categoryBudgetRepository.findAllByBudget_Id(budgetId);
        long totalAmount = categoryBudgets.stream().mapToLong(CategoryBudget::getAmount).sum();
        budget.updateTotalAmount(totalAmount);
    }

}
