package com.evri.interview.controller.dto.request;

import javax.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CourierDto {

	@NotBlank
	private String firstName;
	@NotBlank
	private String lastName;
	private boolean active;

}
