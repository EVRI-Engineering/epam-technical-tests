package com.evri.interview.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CourierForUpdate {

    private String firstName;
    private String lastName;
    private Boolean active;

}
