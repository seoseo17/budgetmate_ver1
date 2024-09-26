package com.budgetmate.api.budgetmate_api.domain.categorybudget.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CategoryBudgetCreateReqeust {
    @Schema(description = "년도", example = "2024")
    @NotBlank
    @Pattern(regexp = "^[0-9]{4}$", message = "4자리 숫자만 입력 가능합니다.")
    private String year;

    @Schema(description = "월", example = "9")
    @NotNull
    @Min(1)
    @Max(12)
    private int month;

    @Schema(description = "카테고리 별 예산(원)", example = "30000")
    @NotNull
    @Min(value = 50, message = "예산 금액은 50원 이상이어야 합니다.")
    private long amount;

    @Schema(description = "카테고리", example = "1")
    @NotNull
    private Long categoryId;
}
