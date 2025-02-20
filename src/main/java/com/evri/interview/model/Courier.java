package com.evri.interview.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Courier {
    long id;
    String firstName;
    String lastName;
    boolean active;
}
