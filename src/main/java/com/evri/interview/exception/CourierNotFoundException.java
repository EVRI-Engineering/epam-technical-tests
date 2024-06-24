package com.evri.interview.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CourierNotFoundException extends RuntimeException {

    private static final String MESSAGE = "Not found courier with id %s";

    public CourierNotFoundException(final Long courierId) {
        super(String.format(MESSAGE, courierId));
    }
}
