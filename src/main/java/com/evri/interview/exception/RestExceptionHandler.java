package com.evri.interview.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(value = { CourierNotFoundException.class })
    protected ResponseEntity<Object> handleCourierNotFoundException(
            CourierNotFoundException ex) {
        log.error("Courier was not found", ex);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }

    @ExceptionHandler(value = { InvalidNameFormatException.class })
    protected ResponseEntity<Object> handleInvalidNameFormatException(
            InvalidNameFormatException ex) {
        log.error("Invalid name format was used", ex);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }
}
