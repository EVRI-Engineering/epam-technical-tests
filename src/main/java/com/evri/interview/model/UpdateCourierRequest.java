package com.evri.interview.model;

import com.evri.interview.controller.validator.NameValidation;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateCourierRequest {

    @NameValidation
    String name;
    boolean active;
}
