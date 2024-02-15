package com.evri.interview.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CourierResponseDto {
    private long id;
    private String name;
    private boolean active;
}
