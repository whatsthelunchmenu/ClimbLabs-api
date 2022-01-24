package com.example.climblabs.global.exception;

import lombok.Builder;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 비즈니스 로직 예외 핸들링
     *
     * @param ex
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = {ClimbLabsException.class})
    public ResponseEntity<ErrorResult> error(ClimbLabsException ex) {
        return new ResponseEntity<>(ErrorResult.of(ex), HttpStatus.valueOf(ex.getHttpStatus()));
    }

    /**
     * Multipart 용량 관련 에러 핸들링
     *
     * @param e
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResult> error(MaxUploadSizeExceededException e) {

        ExceptionCode exceptionCode = ExceptionCode.valueOf(e.getRootCause().getClass().getSimpleName());
        ClimbLabsException climbLabsException = new ClimbLabsException(exceptionCode, e.getRootCause().getMessage());
        return new ResponseEntity<>(ErrorResult.of(climbLabsException), HttpStatus.valueOf(climbLabsException.getHttpStatus()));
    }

    @Value
    @Builder
    static class ErrorResult {
        String errorCode;
        String message;

        static ErrorResult of(ClimbLabsException ex) {
            return ErrorResult
                    .builder()
                    .errorCode(ex.getCode())
                    .message(ex.getMessage())
                    .build();
        }
    }
}
