package com.budgetmate.api.budgetmate_api.domain.expense.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExpenseListRequest {

    @NotNull(message = "필수 입력값 입니다.")
    @Schema(description = "조회 시작 날짜", example = "2024-09-12")
    @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @NotNull(message = "필수 입력값 입니다.")
    @Schema(description = "조회 종료 날짜", example = "2024-09-20")
    @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @Schema(description = "카테고리ID", example = "1")
    private Long categoryId;

    @Schema(description = "최소 금액", example = "1000")
    private Long minAmount;

    @Schema(description = "최대 금액", example = "199000")
    private Long maxAmount;

    @AssertTrue(message = "startDate는 endDate보다 이전 날짜여야 합니다.")
    public boolean isStartDateBeforeEndDate(){
        return !startDate.isAfter(endDate);
    }
}
