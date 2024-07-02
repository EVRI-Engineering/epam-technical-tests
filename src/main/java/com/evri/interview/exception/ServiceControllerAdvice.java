package com.evri.interview.exception;

import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class ServiceControllerAdvice {

	@ExceptionHandler(CourierNotFoundException.class)
	public ResponseEntity<String> handleCourierNotFoundException(CourierNotFoundException e) {
		log.error("Courier not found", e);
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<List<String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
		log.error(e.getMessage(), e);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
			.body(e.getAllErrors().stream().map(ObjectError::toString).collect(Collectors.toList()));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleCommonException(Exception e) {
		log.error(e.getMessage(), e);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong...");
	}
}
