package com.evri.interview.service;

import com.evri.interview.dto.CourierDto;
import com.evri.interview.exception.CourierNotFoundException;
import com.evri.interview.model.Courier;
import com.evri.interview.repository.CourierEntity;
import com.evri.interview.repository.CourierRepository;
import java.util.Collections;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CourierServiceTest {

	private final CourierRepository repository = mock(CourierRepository.class);
	private final CourierTransformer courierTransformer = new CourierTransformer();

	private final CourierService courierService = new CourierService(courierTransformer, repository);

	@Test
	void getAllCouriers_whenActiveFlagIsFalse_thenShouldRetrieveAllCouriers() {

		when(repository.findAll()).thenReturn(Collections.singletonList(CourierEntity.builder()
			.id(1L)
			.firstName("John")
			.lastName("Doe")
			.active(false)
			.build()));

		List<Courier> couriers = courierService.getAllCouriers(false);

		verify(repository).findAll();
		assertEquals(1, couriers.size());
		assertEquals("John Doe", couriers.get(0).getName());
		assertFalse(couriers.get(0).isActive());
	}

	@Test
	void getAllCouriers_whenActiveFlagIsTrue_thenShouldRetrieveOnlyActiveCouriers() {

		when(repository.findAllByActiveTrue()).thenReturn(Collections.singletonList(CourierEntity.builder()
			.id(1L)
			.firstName("John")
			.lastName("Doe")
			.active(true)
			.build()));

		List<Courier> couriers = courierService.getAllCouriers(true);

		verify(repository).findAllByActiveTrue();
		assertEquals(1, couriers.size());
		assertEquals("John Doe", couriers.get(0).getName());
		assertTrue(couriers.get(0).isActive());
	}

	@Test
	void updateCourier() {

		long courierId = 1L;
		CourierDto courierDto = CourierDto.builder()
			.firstName("NewName")
			.lastName("NewLastName")
			.active(true)
			.build();

		when(repository.findById(courierId)).thenReturn(Optional.of(CourierEntity.builder().id(courierId).build()));
		when(repository.save(any(CourierEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

		Courier updatedCourier = courierService.updateCourier(courierId, courierDto);

		assertEquals(courierId, updatedCourier.getId());
		assertEquals("NewName NewLastName", updatedCourier.getName());
		assertTrue(updatedCourier.isActive());
	}

	@Test
	void testUpdateCourierNotFound() {

		long courierId = 1L;
		CourierDto courierDto = CourierDto.builder().build();

		when(repository.findById(courierId)).thenReturn(Optional.empty());

		assertThrows(CourierNotFoundException.class, () -> courierService.updateCourier(courierId, courierDto));
	}
}
