package com.evri.interview.config;

import com.evri.interview.exception.EntityNotFoundException;
import com.evri.interview.model.ErrorData;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorData handleEntityNotFoundException(EntityNotFoundException exception) {
        return ErrorData.builder()
                .errorCode("notFound")
                .errorMessage(exception.getMessage())
                .build();
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorData handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {
        return ErrorData.builder()
                .errorCode("incorrectRequestData")
                .errorMessage(exception.getFieldErrors().stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .collect(Collectors.joining(", ")))
                .build();
    }

    @ExceptionHandler(InvalidFormatException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorData handleInvalidFormatException(InvalidFormatException exception) {
        StringBuilder errorMessage = new StringBuilder("Bad value '")
                .append(exception.getValue() != null ? exception.getValue().toString() : "null").append("' for field '")
                .append(exception.getPath().stream().map(JsonMappingException.Reference::getFieldName).collect(Collectors.joining("','")))
                .append("'.");
        return ErrorData.builder().errorCode("incorrectRequestData").errorMessage(errorMessage.toString()).build();
    }
}
