package com.evri.interview.exception;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@Builder
public class ErrorResponse {

    private HttpStatus httpStatus;
    private String message;
    private final LocalDateTime timestamp;

}
