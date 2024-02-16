package com.evri.interview.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateCourier {

  @NotBlank(message = "First name cannot be blank")
  private String firstName;

  @NotBlank(message = "Last name cannot be blank")
  private String lastName;

  @NotNull(message = "Active status must be provided")
  private Boolean active;
}
