package com.evri.interview.service;

import com.evri.interview.dto.CourierDto;
import com.evri.interview.model.Courier;
import com.evri.interview.repository.CourierEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CourierTransformerTest {

	private final CourierTransformer courierTransformer = new CourierTransformer();

	@Test
	void testToCourier() {
		CourierEntity entity = new CourierEntity();
		entity.setId(1L);
		entity.setFirstName("John");
		entity.setLastName("Doe");
		entity.setActive(true);

		Courier courier = courierTransformer.toCourier(entity);

		assertEquals(1L, courier.getId());
		assertEquals("John Doe", courier.getName());
		assertTrue(courier.isActive());
	}

	@Test
	void testUpdateCourierEntity() {
		CourierEntity entity = new CourierEntity();
		entity.setId(1L);
		entity.setFirstName("OldFirstName");
		entity.setLastName("OldLastName");
		entity.setActive(false);

		CourierDto courierDto = CourierDto.builder()
			.firstName("John")
			.lastName("Doe")
			.active(true)
			.build();

		courierTransformer.updateCourierEntity(entity, courierDto);

		assertEquals("John", entity.getFirstName());
		assertEquals("Doe", entity.getLastName());
		assertTrue(entity.isActive());
	}
}