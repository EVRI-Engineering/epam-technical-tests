package com.evri.interview.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class CourierRequestDto {

    private long id;

    @NotBlank(message = "First name must not be blank")
    @Pattern(regexp = "^[\\p{L}'][ \\p{L}'-]*[\\p{L}]$")
    private String firstName;

    @NotBlank(message = "Last name must not be blank")
    @Pattern(regexp = "^[\\p{L}'][ \\p{L}'-]*[\\p{L}]$")
    private String lastName;

    private boolean isActive;
}
