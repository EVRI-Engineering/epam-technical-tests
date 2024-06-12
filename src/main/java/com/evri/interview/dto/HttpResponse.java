package com.evri.interview.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HttpResponse<C, E> {
    private final C content;
    private final ErrorResponse<E> errors;

    public static <C, E> ResponseEntity<HttpResponse<C,E>> ok(C content) {
        final HttpResponse<C,E> response = HttpResponse
                .<C,E>builder()
                .content(content)
                .build();

        return ResponseEntity.ok(response);
    }

    public static <C, E> ResponseEntity<HttpResponse<C, E>> error(ErrorResponse<E> error) {
        final HttpResponse<C, E> response = HttpResponse
                .<C, E>builder()
                .errors(error)
                .build();

        return ResponseEntity
                .status(error.getStatus())
                .body(response);
    }

}
