package com.evri.interview.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class CourierNameContext {

    private String firstName;

    private String lastName;
}
