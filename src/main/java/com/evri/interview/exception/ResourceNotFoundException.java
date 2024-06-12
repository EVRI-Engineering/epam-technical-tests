package com.evri.interview.exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends EvriRuntimeException {
    public ResourceNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
