package com.evri.interview.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Pattern;

@Data
@Accessors(chain = true)
public class Courier {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    long id;
    @Pattern(regexp = "^\\w+\\s\\w+$", message = "must consist of first and last name")
    String name;
    boolean active;
}
