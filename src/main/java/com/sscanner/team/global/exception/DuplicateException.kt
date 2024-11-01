package com.sscanner.team.global.exception;

import lombok.Getter;

@Getter
public class DuplicateException extends RuntimeException {

    private final int code;
    private final String message;

    public DuplicateException(final ExceptionCode exceptionCode) {
        this.code = exceptionCode.getCode();
        this.message = exceptionCode.getMessage();
    }
}
