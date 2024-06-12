package com.evri.interview.controller.dto;

import com.evri.interview.controller.validation.ValidName;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class CourierDto {

    @NotBlank(message = "Name is mandatory")
    @ValidName
    String name;
    boolean active;
}
