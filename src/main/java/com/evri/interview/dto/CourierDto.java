package com.evri.interview.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourierDto {
    @NotEmpty
    private String firstName;
    @NotEmpty
    private String lastName;
    @NotNull
    private Boolean active;
}
