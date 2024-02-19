package com.evri.interview.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateCourier {

    @NotBlank(message = "'firstName' cannot be blank")
    private String firstName;

    @NotBlank(message = "'lastName' cannot be blank")
    private String lastName;

    @NotNull(message = "'active' status must be provided")
    private Boolean active;
}