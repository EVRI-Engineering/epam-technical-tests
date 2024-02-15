package com.evri.interview.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
@Builder
public class Courier {
    long id;
    @Pattern(regexp = "^(.+) (.+)$")
    String name;
    boolean active;
}
