package com.evri.interview.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CourierRequestBody {
    String firstName;
    String lastName;
    boolean active;
}
