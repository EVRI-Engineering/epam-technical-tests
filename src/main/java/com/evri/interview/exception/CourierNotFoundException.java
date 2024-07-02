package com.evri.interview.exception;

public class CourierNotFoundException extends RuntimeException {


	public CourierNotFoundException(long courierId) {
		super("Courier with id [%s] not found".formatted(courierId));
	}
}
