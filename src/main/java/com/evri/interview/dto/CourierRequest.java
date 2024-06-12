package com.evri.interview.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourierRequest {

    @NotBlank(message = "First name should not be blank.")
    @Size(max = 64, message = "First name should be less then 64 characters.")
    private String firstName;
    @NotBlank(message = "Last name should not be blank.")
    @Size(max = 64, message = "Last name should be less then 64 characters.")
    private String lastName;
    @NotNull(message = "Active status must be provided.")
    private Boolean active;
}
