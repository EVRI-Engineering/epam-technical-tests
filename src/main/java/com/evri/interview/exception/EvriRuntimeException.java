package com.evri.interview.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class EvriRuntimeException extends RuntimeException {
    private final HttpStatus status;

    public EvriRuntimeException(HttpStatus status) {
        this.status = status;
    }

    public EvriRuntimeException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public EvriRuntimeException(String message, Throwable cause, HttpStatus status) {
        super(message, cause);
        this.status = status;
    }
}
