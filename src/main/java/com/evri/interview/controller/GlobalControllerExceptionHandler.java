package com.evri.interview.controller;

import com.evri.interview.exception.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * A global exception handler class that intercepts exceptions thrown.
 * This class is annotated with {@code @RestControllerAdvice}, making it capable of handling exceptions
 * from all controllers marked with {@code @RequestMapping}.
 *
 * <p>It provides a centralized approach to exception handling, ensuring consistency and cleanliness
 * in the way exceptions are processed and responses are generated.</p>
 */
@RestControllerAdvice
public class GlobalControllerExceptionHandler {
    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleEntityNotFound(RuntimeException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
}
