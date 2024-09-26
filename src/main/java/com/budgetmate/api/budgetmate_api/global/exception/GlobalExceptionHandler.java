package com.budgetmate.api.budgetmate_api.global.exception;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import ch.qos.logback.classic.Logger;
import com.budgetmate.api.budgetmate_api.global.CommonResponse;
import com.budgetmate.api.budgetmate_api.global.error.ErrorResponse;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String ERROR_MESSAGE_DELIMITER = "," + System.lineSeparator();


    @ExceptionHandler(value = CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException customException) {
        ErrorResponse errorResponse = new ErrorResponse(customException.getErrorCode(),
            customException.getMessage());
        return new ResponseEntity<>(errorResponse, errorResponse.getHttpStatus());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<CommonResponse<Void>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("[HandleMethodArgumentNotValidException]", e);
        BindingResult bindingResult = e.getBindingResult();

        List<String> errorMessage = new ArrayList<>();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            errorMessage.add("[" + fieldError.getField() + "] " + fieldError.getDefaultMessage());
        }

        return ResponseEntity.badRequest().body(CommonResponse.fail(String.join(
            ERROR_MESSAGE_DELIMITER,
            errorMessage)
        ));
    }

}
