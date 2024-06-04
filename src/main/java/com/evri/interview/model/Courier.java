package com.evri.interview.model;

import com.evri.interview.model.constraints.CourierNameValidator;
import com.evri.interview.model.constraints.UpdateRequest;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Courier {

  @Null(groups = UpdateRequest.class, message = "Field 'id' must be absent for update request.")
  private Long id;

  @NotNull
  @CourierNameValidator(groups = {UpdateRequest.class})
  private String name;

  private Boolean active;
}
