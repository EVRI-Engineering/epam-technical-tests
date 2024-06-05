package com.evri.interview.model;

import com.evri.interview.model.constraints.UpdateRequest;
import java.util.Comparator;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Courier implements Comparable<Courier> {

  @Null(groups = UpdateRequest.class, message = "Field 'id' must be absent for update request.")
  private Long id;

  @NotNull
  @Size(min = 1, max = 44, message = "size must be up to 40 characters", groups = UpdateRequest.class)
  @Pattern(
      regexp = "[a-zA-Z-]+\\s[a-zA-Z-]+$",
      message = "Name must contains only alpha characters and splitted by one space.",
      groups = {UpdateRequest.class})
  private String name;

  private Boolean active;


  @Override
  public int compareTo(Courier courier) {
    return Comparator.comparing(Courier::getName)
        .thenComparing(Courier::getActive)
        .compare(this, courier);
  }
}
