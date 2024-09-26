package com.budgetmate.api.budgetmate_api.domain.expense.service;

import static com.budgetmate.api.budgetmate_api.global.error.ErrorCode.ACCESS_DENIED;
import static com.budgetmate.api.budgetmate_api.global.error.ErrorCode.EXPENSE_NOT_FOUND;

import com.budgetmate.api.budgetmate_api.domain.category.service.CategoryService;
import com.budgetmate.api.budgetmate_api.domain.expense.dto.ExpenseDto;
import com.budgetmate.api.budgetmate_api.domain.expense.dto.request.ExpenseCreateRequest;
import com.budgetmate.api.budgetmate_api.domain.expense.dto.request.ExpenseListRequest;
import com.budgetmate.api.budgetmate_api.domain.expense.dto.response.ExpenseListResponse;
import com.budgetmate.api.budgetmate_api.domain.expense.entity.Expense;
import com.budgetmate.api.budgetmate_api.domain.expense.repository.ExpenseRepository;
import com.budgetmate.api.budgetmate_api.domain.user.entity.User;
import com.budgetmate.api.budgetmate_api.domain.user.service.UserService;
import com.budgetmate.api.budgetmate_api.global.exception.CustomException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final UserService userService;
    private final CategoryService categoryService;

    public void createExpense(Long userId, ExpenseCreateRequest dto){
        User user = userService.findById(userId);

        Expense expense = createExpenseEntity(user,dto);
        expenseRepository.save(expense);
    }

    public void updateExpense(Long userId, Long expenseId, ExpenseCreateRequest dto){
        Expense expense = expenseRepository.findByIdAndUser_Id(expenseId, userId)
            .orElseThrow(() -> new CustomException(ACCESS_DENIED));

        expense.updateExpense(dto.getExpenseDate(),dto.getMemo(),dto.getAmount(), dto.getCategoryId(),dto.isExcludedSum());
        expenseRepository.save(expense);
    }

    public void deleteExpense(Long userId,Long expendId){
        Expense expense = expenseRepository.findByIdAndUser_Id(expendId, userId)
            .orElseThrow(() -> new CustomException(ACCESS_DENIED));
        expenseRepository.delete(expense);
    }

    public ExpenseDto getExpenseDetail(Long userId,Long expendId){
        Expense expense = findById(expendId);
        if (!isUserExpense(userId,expense)){
            throw new CustomException(ACCESS_DENIED);
        }

        String category = categoryService.findNameById(expense.getCategoryId());
        return new ExpenseDto().fromEntity(expense,category);

    }
    /** 모든 카테고리 목록 조회*/
    public ExpenseListResponse getExpenseList(Long userId, ExpenseListRequest dto){
        LocalDate strDate = dto.getStartDate();
        LocalDate endDate = dto.getEndDate();
        long minAmount = dto.getMinAmount()!= null ? dto.getMinAmount() : 0L;
        long maxAmount = dto.getMaxAmount()!= null ? dto.getMaxAmount() : Long.MAX_VALUE;
        //1. 목록 조회
        List<Expense> expenseList= expenseRepository.findExpensesByUserAndDateAndPrice(userId,strDate,endDate,minAmount,maxAmount);
        List<ExpenseDto> expenseDtos = convertToExpenseDtoList(expenseList);

        //2. 지출 합계 (합계제외는 제외)
        long totalExpense = sumTotalExpense(expenseDtos);

        //3. 카테고리별 지출 합계(합계제외는 제외)
        Map<String,Long> totalExpenseByCategory = expenseDtos.stream()
            .collect(Collectors.groupingBy(ExpenseDto::getCategory,
                Collectors.summingLong(ExpenseDto::getAmount)));

        return new ExpenseListResponse(expenseDtos,totalExpense,totalExpenseByCategory);
    }

    /** 카테고리별 목록 조회*/
    public ExpenseListResponse getExpenseListByCategory(Long userId, ExpenseListRequest dto){
        LocalDate strDate = dto.getStartDate();
        LocalDate endDate = dto.getEndDate();
        long minAmount = dto.getMinAmount()!= null ? dto.getMinAmount() : 0L;
        long maxAmount = dto.getMaxAmount()!= null ? dto.getMaxAmount() : Long.MAX_VALUE;
        Long categoryId = dto.getCategoryId();

        List<Expense> expenseList = expenseRepository.findExpensesByCategoryAndUserAndDateAndPrice(
            userId,categoryId,strDate,endDate,minAmount,maxAmount);

        List<ExpenseDto> expenseDtos = convertToExpenseDtoList(expenseList);

        //2. 지출 합계 (합계제외는 제외)
        long totalExpense = sumTotalExpense(expenseDtos);

        return new ExpenseListResponse(expenseDtos,totalExpense,null);
    }



    public Expense findById(Long expenseId){
        return expenseRepository.findById(expenseId).orElseThrow(
            () -> new CustomException(EXPENSE_NOT_FOUND));
    }

    private List<ExpenseDto> convertToExpenseDtoList(List<Expense> expenseList){
        return expenseList.stream().map(
            expense -> {
                String category = categoryService.findNameById(expense.getCategoryId());
                return new ExpenseDto().fromEntity(expense,category);
            }).toList();
    }

    /** 지출 합계(합계제외 처리한 지출은 제외)*/
    private long sumTotalExpense(List<ExpenseDto> expenseDtos){
        return expenseDtos.stream().filter(expenseDto -> !expenseDto.isExcludedSum())
            .mapToLong(ExpenseDto::getAmount).sum();
    }

    private Expense createExpenseEntity(User user,ExpenseCreateRequest dto){
        return Expense.builder()
            .user(user)
            .categoryId(dto.getCategoryId())
            .expenseAt(dto.getExpenseDate())
            .amount(dto.getAmount())
            .memo(dto.getMemo())
            .excludedSum(dto.isExcludedSum())
            .build();
    }

    private boolean isUserExpense(Long userId, Expense expense){
        return expense.getUser().getId().equals(userId);
    }
}
