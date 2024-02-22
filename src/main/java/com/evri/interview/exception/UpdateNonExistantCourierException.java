package com.evri.interview.exception;

public class UpdateNonExistantCourierException extends RuntimeException {
    public UpdateNonExistantCourierException(String message) {
        super(message);
    }
}
