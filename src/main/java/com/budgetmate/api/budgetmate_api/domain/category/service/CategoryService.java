package com.budgetmate.api.budgetmate_api.domain.category.service;

import static com.budgetmate.api.budgetmate_api.global.error.ErrorCode.CATEGORY_NOT_FOUND;

import com.budgetmate.api.budgetmate_api.domain.category.dto.CategoryDto;
import com.budgetmate.api.budgetmate_api.domain.category.entity.Category;
import com.budgetmate.api.budgetmate_api.domain.category.repository.CategoryRepository;
import com.budgetmate.api.budgetmate_api.global.exception.CustomException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService {


    private final CategoryRepository categoryRepository;


    public List<CategoryDto> getCategoryList(){

        List<Category> list = categoryRepository.findAll();
        return list.stream().map(category -> new CategoryDto(category))
            .collect(Collectors.toList());
    }

    public Category findById(Long id){
        return categoryRepository.findById(id).orElseThrow(()->new CustomException(CATEGORY_NOT_FOUND));
    }

    public Category findByName(String name){
        return categoryRepository.findByName(name).orElseThrow(()->new CustomException(CATEGORY_NOT_FOUND));
    }

}

