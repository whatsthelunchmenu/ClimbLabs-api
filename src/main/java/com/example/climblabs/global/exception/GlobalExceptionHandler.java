package com.example.climblabs.global.exception;

import lombok.Builder;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = ClimbLabsException.class)
    public ResponseEntity<ErrorResult> error(ClimbLabsException ex) {
        return new ResponseEntity<>(ErrorResult.of(ex), HttpStatus.valueOf(ex.getHttpStatus()));
    }

    @Value
    @Builder
    static class ErrorResult {
        private String errorCode;
        private String message;

        static ErrorResult of(ClimbLabsException ex){
            return ErrorResult
                    .builder()
                    .errorCode(ex.getCode())
                    .message(ex.getMessage())
                    .build();
        }
    }
}
