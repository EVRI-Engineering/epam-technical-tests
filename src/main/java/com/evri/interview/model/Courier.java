package com.evri.interview.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Courier {
    Long id;
    String name;
    Boolean active;
}
