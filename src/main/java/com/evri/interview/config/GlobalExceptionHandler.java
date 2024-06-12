package com.evri.interview.config;

import java.time.LocalDateTime;

import com.evri.interview.dto.ErrorResponse;
import com.evri.interview.dto.HttpResponse;
import com.evri.interview.exception.EvriRuntimeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({EvriRuntimeException.class})
    public ResponseEntity<HttpResponse<Object, Object>> handleEvriRuntimeException(EvriRuntimeException ex) {
        ErrorResponse<Object> errorResponse = ErrorResponse.builder()
                .status(ex.getStatus())
                .message(ex.getMessage())
                .currentDatetime(LocalDateTime.now())
                .build();

        return HttpResponse.error(errorResponse);
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class, HttpMessageNotReadableException.class})
    public ResponseEntity<HttpResponse<Object, Object>> handleHttpMessageNotReadableException(Exception ex) {
        ErrorResponse<Object> errorResponse = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message(ex.getMessage())
                .currentDatetime(LocalDateTime.now())
                .build();

        return HttpResponse.error(errorResponse);
    }
}
