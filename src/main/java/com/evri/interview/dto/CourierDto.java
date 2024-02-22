package com.evri.interview.dto;

import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;


@Data
@Builder
public class CourierDto {
    @NotBlank
    @Length(max = 64)
    @Pattern(regexp = "^[A-Z][a-z]*$", message = "Name should contains only latin letters")
    private String name;

    @NotBlank
    @Length(max = 64)
    @Pattern(regexp = "^[A-Z][a-z]*$", message = "Lastname should contains only latin letters")
    private String lastName;

    @NotNull
    private boolean isActive;
}
