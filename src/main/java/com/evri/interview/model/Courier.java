package com.evri.interview.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Courier {
  long id;
  String name;
  boolean active;
}
