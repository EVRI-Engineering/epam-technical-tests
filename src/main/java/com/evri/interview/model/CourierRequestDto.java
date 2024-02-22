package com.evri.interview.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class CourierRequestDto {

    @NotBlank(message = "Courier first name is required")
    String firstName;
    @NotBlank(message = "Courier last name is required")
    String lastName;
    boolean active;

}
