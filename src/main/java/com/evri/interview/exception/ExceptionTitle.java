package com.evri.interview.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExceptionTitle {
  NOT_FOUND("Not Found"),
  BAD_REQUEST("Bad Request"),
  SERVICE_ERROR("Service error"),
  DATABASE_ERROR("Database error"),
  UNEXPECTED_EXCEPTION("Unexpected exception");

  private final String title;
}
