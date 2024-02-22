package com.evri.interview.handlers;

import com.evri.interview.exception.UpdateNonExistantCourierException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class CourierExceptionHandler {

    @ExceptionHandler(value = UpdateNonExistantCourierException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public String handleValidationException(UpdateNonExistantCourierException exception) {
        String exceptionMessage = exception.getMessage();
        log.warn(exceptionMessage);
        return exceptionMessage;
    }
}
