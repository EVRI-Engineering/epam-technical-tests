package com.evri.interview.controller.exception;

import java.text.MessageFormat;

public class CourierNotFoundException extends RuntimeException {

    public CourierNotFoundException(String message, Object... arg) {
        super(MessageFormat.format(message, arg));
    }
}
