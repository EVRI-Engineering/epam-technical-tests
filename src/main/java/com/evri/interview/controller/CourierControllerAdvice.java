package com.evri.interview.controller;

import com.evri.interview.exception.CourierNotFound;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CourierControllerAdvice {
    @ExceptionHandler(CourierNotFound.class)
    public ResponseEntity<?> handleCourierNotFound(CourierNotFound ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }
}
