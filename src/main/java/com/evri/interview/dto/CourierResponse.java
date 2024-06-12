package com.evri.interview.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CourierResponse {
    long id;
    String name;
    boolean active;
}
