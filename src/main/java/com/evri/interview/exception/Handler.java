package com.evri.interview.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class Handler {

	@ExceptionHandler(CourierNotFoundException.class)
	public ResponseEntity<Object> handleCourierNotFoundException(CourierNotFoundException exception) {
		log.error("Courier not found", exception);
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleCommonException(Exception e) {
		log.error(e.getMessage(), e);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong...");
	}
}
