package com.evri.interview.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class CourierDTO {
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotNull
    private Boolean active;
}
