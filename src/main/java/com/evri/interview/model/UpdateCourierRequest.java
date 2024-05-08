package com.evri.interview.model;

import lombok.Data;

@Data
public class UpdateCourierRequest {

    private String firstName;
    private String lastName;
    private boolean active;
}
