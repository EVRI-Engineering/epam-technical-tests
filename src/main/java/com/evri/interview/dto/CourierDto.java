package com.evri.interview.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class CourierDto {
    private String firstName;
    private String lastName;
    private boolean active;
}
