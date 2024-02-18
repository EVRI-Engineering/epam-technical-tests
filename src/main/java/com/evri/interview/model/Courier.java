package com.evri.interview.model;

import com.evri.interview.validator.annotation.FullName;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Courier {
    long id;
    @FullName
    String name;
    boolean active;
}
