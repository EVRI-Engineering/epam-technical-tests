package com.evri.interview.request;

import javax.validation.constraints.NotBlank;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CourierRequestBody {
    @NotBlank(message = "firstName is required")
    String firstName;
    @NotBlank(message = "lastName is required")
    String lastName;
    boolean active;
}
