package com.evri.interview.exception.handler;

import com.evri.interview.exception.CourierException;
import com.evri.interview.exception.ExceptionReason;
import com.evri.interview.exception.ExceptionTitle;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class CourierExceptionHandlers {

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String, Object>> onUnhandledException(Exception e) {

    log.error("Unexpected exception", e);

    Map<String, Object> map = new LinkedHashMap<>();
    map.put("title", "Service error");
    map.put("detail", e.getMessage());
    map.put("reason", "Unexpected exception");
    return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(map);
  }

  @ExceptionHandler(CourierException.class)
  public ResponseEntity<Map<String, Object>> onCourierException(CourierException e) {
    Map<String, Object> map = new LinkedHashMap<>();
    map.put("title", e.getTitle());
    map.put("reason", e.getReason().toString());
    map.put("detail", e.getMessage() == null ? e.getStatus() : e.getMessage());
    return ResponseEntity.status(e.getStatus()).body(map);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, Object>> onMethodArgumentNotValidException(
      MethodArgumentNotValidException e) {
    log.error(e.getMessage());

    Map<String, Object> map = new LinkedHashMap<>();
    map.put("title", ExceptionTitle.BAD_REQUEST.getTitle());
    map.put("reason", ExceptionReason.BAD_REQUEST.name());
    map.put("detail", buildDetails(e));
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
  }

  private String buildDetails(MethodArgumentNotValidException e) {
    FieldError fieldError = e.getBindingResult().getFieldError();
    if (fieldError != null) {
      return fieldError.getDefaultMessage();
    }
    return e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
  }
}
