package com.evri.interview.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.evri.interview.exception.ResourceNotFoundException;

import lombok.extern.slf4j.Slf4j;

/**
 * ResourceNotFoundExceptionHendler
 */
@Slf4j
@ControllerAdvice
public class CourierExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
        ResourceNotFoundException resourceNotFoundException
    ) {
        log.warn("ResourceNotFoundException: {}", resourceNotFoundException.getMessage());

        return new ResponseEntity<>(
            new ErrorResponse(resourceNotFoundException.getMessage()), HttpStatus.NOT_FOUND
        );
    }
}