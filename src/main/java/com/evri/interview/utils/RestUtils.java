package com.evri.interview.utils;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

import com.evri.interview.dto.ErrorResponse;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

public final class RestUtils {

    private RestUtils() {
        throw new UnsupportedOperationException("It is forbidden to create an instance of the class.");
    }

    public static ErrorResponse<Map<String, String>> mapErrorResponse(BindingResult bindingResult) {
        final Map<String, String> errors = bindingResult.getFieldErrors()
                .stream()
                .collect(Collectors.toMap(FieldError::getField, DefaultMessageSourceResolvable::getDefaultMessage));

        return ErrorResponse
                .<Map<String, String>>builder()
                .message("The data is filled incorrectly.")
                .currentDatetime(LocalDateTime.now())
                .additionalInfo(errors)
                .status(HttpStatus.BAD_REQUEST)
                .build();

    }
}
