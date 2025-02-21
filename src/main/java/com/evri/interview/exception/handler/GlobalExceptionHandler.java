package com.evri.interview.exception.handler;

import com.evri.interview.dto.ErrorDto;
import com.evri.interview.exception.CourierNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(CourierNotFoundException.class)
	public ResponseEntity<ErrorDto> handleException(Exception e) {
		return new ResponseEntity<>(new ErrorDto(e.getMessage()), HttpStatus.NOT_FOUND);
	}
}