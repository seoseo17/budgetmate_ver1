package com.budgetmate.api.budgetmate_api.domain.categorybudget.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CategoryBudgetUpdateReqeust {

    @Schema(description = "카테고리 별 예산(원)", example = "30000")
    @NotNull
    @Min(value = 50, message = "예산 금액은 50원 이상이어야 합니다.")
    private long amount;

    @Schema(description = "카테고리ID", example = "1")
    @NotNull
    private Long categoryId;
}
