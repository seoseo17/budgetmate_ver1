package com.budgetmate.api.budgetmate_api.domain.expense.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ExpenseCreateRequest {

    @Schema(description = "지출 금액", example = "30000")
    @NotNull
    @Min(value = 1, message = "예산 금액은 1원 이상이어야 합니다.")
    private int amount;

    @Schema(description = "카테고리", example = "1")
    @NotNull
    private Long categoryId;

    @Schema(description = "메모", example = "1주일치 장보기")
    private String memo;

    @Schema(description = "지출 날짜", example = "2024-09-20")
    @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd")
    @NotNull
    private LocalDate expenseDate;

}
