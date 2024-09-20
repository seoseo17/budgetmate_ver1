package com.budgetmate.api.budgetmate_api.domain.categorybudget.service;

import static com.budgetmate.api.budgetmate_api.global.error.ErrorCode.CATEGORYBUDGET_NOT_FOUND;
import static com.budgetmate.api.budgetmate_api.global.error.ErrorCode.INVALID_PATH;

import com.budgetmate.api.budgetmate_api.domain.budget.entity.Budget;
import com.budgetmate.api.budgetmate_api.domain.budget.repository.BudgetRepository;
import com.budgetmate.api.budgetmate_api.domain.budget.service.BudgetService;
import com.budgetmate.api.budgetmate_api.domain.category.entity.Category;
import com.budgetmate.api.budgetmate_api.domain.category.service.CategoryService;
import com.budgetmate.api.budgetmate_api.domain.categorybudget.dto.CategoryBudgetCreateReqeust;
import com.budgetmate.api.budgetmate_api.domain.categorybudget.dto.CategoryBudgetUpdateReqeust;
import com.budgetmate.api.budgetmate_api.domain.categorybudget.entity.CategoryBudget;
import com.budgetmate.api.budgetmate_api.domain.categorybudget.repository.CategoryBudgetRepository;
import com.budgetmate.api.budgetmate_api.domain.user.entity.User;
import com.budgetmate.api.budgetmate_api.domain.user.service.UserService;
import com.budgetmate.api.budgetmate_api.global.exception.CustomException;
import com.budgetmate.api.budgetmate_api.global.security.userDetails.CustomUserDetails;
import java.util.List;
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

    public void updateBudgetByCategory(Long userId, Long categoryBudgetId, Category category, CategoryBudgetUpdateReqeust dto){
        int amount = dto.getAmount();
        CategoryBudget currentCategoryBudget = findById(categoryBudgetId);
        Long budgetId = currentCategoryBudget.getBudget().getId();
        Long newCategoryId = category.getId();

        List<Long> categoryIds = categoryBudgetRepository.findCategoryIdsByBudgetId(budgetId);

        if (!categoryIds.contains(category.getId())){
            log.info("[ERROR] budgetId: {}의 category: {} 존재하지 않음",budgetId,category.getId());
            throw new CustomException(INVALID_PATH);
        }

        // 카테고리가 변경된다면
        if (!currentCategoryBudget.getCategory().getId().equals(newCategoryId)){
            CategoryBudget updatedCategoryBudget = findByCategoryIdAndBudgetId(newCategoryId,budgetId);
            updatedCategoryBudget.updateAmount(amount);
            categoryBudgetRepository.save(updatedCategoryBudget);

            // 기존 카테고리의 예산 조정
            int difference = Math.max(currentCategoryBudget.getAmount() - amount, 0);

            currentCategoryBudget.setAmount(difference);
            categoryBudgetRepository.save(currentCategoryBudget);

        }else {
            // 예산 값만 변경
            currentCategoryBudget.setAmount(amount);
            categoryBudgetRepository.save(currentCategoryBudget);
        }

        budgetService.recalculateTatolAmount(budgetId);
    }

    public void deleteBudgetByCategory(Long userId, Long categoryBudgetId){

        CategoryBudget categoryBudget = findById(categoryBudgetId);
        categoryBudgetRepository.delete(categoryBudget);
        budgetService.recalculateTatolAmount(categoryBudget.getBudget().getId());

    }



    public CategoryBudget findById(Long id){
        return categoryBudgetRepository.findById(id).orElseThrow(
            ()-> new CustomException(CATEGORYBUDGET_NOT_FOUND));
    }

    public CategoryBudget findByCategoryIdAndBudgetId(Long categoryId, Long budgetId){
        return categoryBudgetRepository.findByCategory_IdAndBudget_Id(categoryId,budgetId);
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
