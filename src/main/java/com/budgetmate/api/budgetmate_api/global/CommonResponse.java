package com.budgetmate.api.budgetmate_api.global;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
@AllArgsConstructor
@Getter
public class CommonResponse<T> {
    private final String message;
    private final T data;


    public static <T> CommonResponse<T> ok(T data) {
        return new CommonResponse<>("Success", data);
    }

    public static <T> CommonResponse<T> ok(String message, T data) {
        return new CommonResponse<>(message, data);
    }

    public static CommonResponse<Void> fail(String message){
        return new CommonResponse<>(message,null);
    }

}
