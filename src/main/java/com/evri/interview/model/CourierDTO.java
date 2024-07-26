package com.evri.interview.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CourierDTO {
  private String firstName;
  private String lastName;
  private boolean active;
}
