package com.evri.interview.controller.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CourierDto {

	private String firstName;
	private String lastName;
	private boolean active;

}
