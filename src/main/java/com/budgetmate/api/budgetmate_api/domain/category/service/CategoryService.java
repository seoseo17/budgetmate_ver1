package com.budgetmate.api.budgetmate_api.domain.category.service;

import com.budgetmate.api.budgetmate_api.domain.category.dto.CategoryDto;
import com.budgetmate.api.budgetmate_api.domain.category.entity.Category;
import com.budgetmate.api.budgetmate_api.domain.category.repository.CategoryRepository;
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
}
