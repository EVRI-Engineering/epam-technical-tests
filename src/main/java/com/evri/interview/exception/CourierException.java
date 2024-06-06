package com.evri.interview.exception;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class CourierException extends RuntimeException {

  private final ExceptionReason reason;
  private final HttpStatus status;
  private final String title;
}
