package com.budgetmate.api.budgetmate_api.domain.categorybudget.service;

import static com.budgetmate.api.budgetmate_api.global.error.ErrorCode.INVALID_PATH;

import com.budgetmate.api.budgetmate_api.domain.budget.entity.Budget;
import com.budgetmate.api.budgetmate_api.domain.budget.repository.BudgetRepository;
import com.budgetmate.api.budgetmate_api.domain.budget.service.BudgetService;
import com.budgetmate.api.budgetmate_api.domain.category.entity.Category;
import com.budgetmate.api.budgetmate_api.domain.category.service.CategoryService;
import com.budgetmate.api.budgetmate_api.domain.categorybudget.dto.CategoryBudgetCreateReqeust;
import com.budgetmate.api.budgetmate_api.domain.categorybudget.entity.CategoryBudget;
import com.budgetmate.api.budgetmate_api.domain.categorybudget.repository.CategoryBudgetRepository;
import com.budgetmate.api.budgetmate_api.domain.user.entity.User;
import com.budgetmate.api.budgetmate_api.domain.user.service.UserService;
import com.budgetmate.api.budgetmate_api.global.exception.CustomException;
import com.budgetmate.api.budgetmate_api.global.security.userDetails.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryBudgetService {

    private final BudgetService budgetService;
    private final CategoryService categoryService;
    private final UserService userService;


    private final CategoryBudgetRepository categoryBudgetRepository;
    private final BudgetRepository budgetRepository;


    public void createBudgetByCategory(Long userId, Category category, CategoryBudgetCreateReqeust dto){

        int year = Integer.parseInt(dto.getYear());
        int month = dto.getMonth();
        User user = userService.findById(userId);

        //1. 월별 예산 존재 확인
        Budget budget = budgetService.checkBudgetExists(year,dto.getMonth()) ?
            budgetService.getBudgetByUserIdAndYearAndMonth(user.getId(), year,month) : createNewBudget(user,year,month);

        //2. 월별 카테고리 예산 존재 확인
        if (categoryBudgetRepository.existsByCategory_IdAndBudget_Id(category.getId(),
            budget.getId())){
            throw new CustomException(INVALID_PATH);
        }else {
            createNewCategoryBudget(dto,budget,category);
            budgetService.recalculateTatolAmount(budget.getId());
        }
    }

    private Budget createNewBudget(User user, int year, int month) {
        Budget budget = Budget.builder()
            .user(user)
            .year(year)
            .month(month)
            .build();
        return budgetRepository.save(budget);
    }

    private void createNewCategoryBudget(CategoryBudgetCreateReqeust dto, Budget budget, Category category) {
        CategoryBudget categoryBudget = CategoryBudget.builder()
            .amount(dto.getAmount())
            .budget(budget)
            .category(category)
            .build();
        categoryBudgetRepository.save(categoryBudget);
    }

}
