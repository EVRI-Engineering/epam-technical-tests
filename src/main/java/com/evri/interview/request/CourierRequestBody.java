package com.evri.interview.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CourierRequestBody {

    @NotBlank(message = "firstName is required")
    @Pattern(regexp = "^[A-Z][a-z]*$", message = "Invalid firstName format")
    String firstName;

    @NotBlank(message = "lastName is required")
    @Pattern(regexp = "^[A-Z][a-z]*$", message = "Invalid lastName format")
    String lastName;

    boolean active;
}
