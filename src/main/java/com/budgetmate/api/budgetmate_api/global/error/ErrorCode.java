package com.budgetmate.api.budgetmate_api.global.error;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    //공통
    INVALID_INPUT_VALUE(BAD_REQUEST, "유효하지 않은 입력 값입니다."),
    COMMON_SYSTEM_ERROR(INTERNAL_SERVER_ERROR, "시스템 오류입니다."),
    INVALID_PATH(BAD_REQUEST, "잘못된 경로 입니다."),
    REDIS_SERVER_ERROR(BAD_REQUEST, "Redis 서버에서 오류가 발생했습니다."),

    //User
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "사용자를 찾을 수 없습니다."),
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다."),

    // 인증&인가
    AUTHENTICATION_FAILED(HttpStatus.UNAUTHORIZED, "사용자 인증에 실패했습니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),
    AUTHORIZATION_HEADER_MISSING(HttpStatus.UNAUTHORIZED, "Authorization 헤더값이 유효하지 않습니다."),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "토큰 인증 시간이 만료되었습니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "토큰 형식이 유효하지 않습니다."),
    ACCESS_TOKEN_HEADER_MISSING(HttpStatus.UNAUTHORIZED, "access 토큰 헤더값이 유효하지 않습니다."),
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "refresh 토큰을 찾을 수 없습니다."),



    //Budget
    BUDGET_NOT_FOUND(HttpStatus.BAD_REQUEST, "존재하지 않는 예산 목록입니다."),

    //Category
    CATEGORY_NOT_FOUND(HttpStatus.BAD_REQUEST, "존재하지 않는 카테고리입니다."),

    //CategoryBudget
    CATEGORYBUDGET_NOT_FOUND(HttpStatus.BAD_REQUEST, "존재하지 않는 카테고리별 예산 목록입니다."),

    //Expense
    EXPENSE_NOT_FOUND(HttpStatus.BAD_REQUEST, "존재하지 않는 지출 목록입니다.")

    ;

    private final HttpStatus httpStatus;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
