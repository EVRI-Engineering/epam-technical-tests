package com.evri.interview.exception;

public class CourierNotFoundException extends RuntimeException {


	public CourierNotFoundException(long courierId) {
		super(String.format("Courier with id [%s] not found", courierId));
	}
}
