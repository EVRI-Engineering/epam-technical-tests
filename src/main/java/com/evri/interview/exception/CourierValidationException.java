package com.evri.interview.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CourierValidationException extends RuntimeException {

    public CourierValidationException(String courierName) {
        super(String.format("Courier name is not valid: %s", courierName));
    }
}
