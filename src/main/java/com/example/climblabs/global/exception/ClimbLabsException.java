package com.example.climblabs.global.exception;

import lombok.Getter;

public class ClimbLabsException extends RuntimeException {

    private ExceptionCode code;
    private int httpStatus;
    private String message;

    public ClimbLabsException(ExceptionCode code){
        this.code = code;
        this.message = code.getMessage();
        this.httpStatus = code.getStatus();
    }

    public ClimbLabsException(ExceptionCode code, String message) {
        this.code = code;
        this.httpStatus = code.getStatus();
        this.message = message;
    }

    public ClimbLabsException(ExceptionCode code, int httpStatus) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.message = code.getMessage();
    }

    public String getCode() {
        return code.name();
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
