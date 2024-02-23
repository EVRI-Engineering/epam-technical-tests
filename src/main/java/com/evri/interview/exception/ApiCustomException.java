package com.evri.interview.exception;

public class ApiCustomException extends RuntimeException {

    public ApiCustomException() {
        super();
    }

    public ApiCustomException(String message) {
        super(message);
    }

    public ApiCustomException(String message, Throwable cause) {
        super(message, cause);
    }
}
