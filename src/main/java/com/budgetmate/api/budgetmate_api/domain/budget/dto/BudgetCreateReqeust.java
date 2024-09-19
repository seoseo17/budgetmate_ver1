package com.budgetmate.api.budgetmate_api.domain.budget.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class BudgetCreateReqeust {

    @Schema(description = "년도", example = "2024")
    @NotNull
    @Pattern(regexp = "^[0-9]{4}$", message = "4자리 숫자만 입력 가능합니다.")
    private String year;

    @Schema(description = "월", example = "9")
    @NotNull
    @Min(1)
    @Max(12)
    private int month;

    @Schema(description = "월별 총 예산(원)", example = "100000")
    @NotNull
    private int totalAmount;
}
