package com.evri.interview.exception;

public class CourierNotFound extends RuntimeException {
    public CourierNotFound(String message) {
        super(message);
    }
}
