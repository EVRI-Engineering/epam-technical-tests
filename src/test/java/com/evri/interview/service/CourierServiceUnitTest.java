package com.evri.interview.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.evri.interview.TestUtils;
import com.evri.interview.controller.dto.request.CourierDto;
import com.evri.interview.exception.CourierNotFoundException;
import com.evri.interview.mapper.CourierMapperImpl;
import com.evri.interview.model.Courier;
import com.evri.interview.repository.CourierEntity;
import com.evri.interview.repository.CourierRepository;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CourierServiceUnitTest {

	@Mock
	CourierRepository courierRepository;

	@Test
	void getCouriers_filterByIsActiveIsRequired_onlyActiveCouriersReturned() {
		//Given
		doReturn(TestUtils.listOf(
			CourierEntity.builder().id(1L).firstName("John").lastName("Carter").active(true).build(),
			CourierEntity.builder().id(2L).firstName("Amy").lastName("Carter").active(true).build()
		)).when(courierRepository).getAllByActiveIsTrue();

		CourierService courierService = new CourierService(courierRepository, new CourierMapperImpl());

		//When
		List<Courier> couriers = courierService.getCouriers(true);

		//Then
		assertThat(couriers)
			.containsExactlyInAnyOrderElementsOf(TestUtils.listOf(
				Courier.builder().id(1L).name("John Carter").active(true).build(),
				Courier.builder().id(2L).name("Amy Carter").active(true).build()
			));
		verify(courierRepository, times(1)).getAllByActiveIsTrue();

	}

	@Test
	void getCouriers_filterByIsActiveIsDisabled_allCouriersReturned() {
		//Given
		doReturn(TestUtils.listOf(
			CourierEntity.builder().id(1L).firstName("John").lastName("Carter").active(true).build(),
			CourierEntity.builder().id(2L).firstName("Amy").lastName("Carter").active(false).build()
		)).when(courierRepository).findAll();

		CourierService courierService = new CourierService(courierRepository, new CourierMapperImpl());

		//When
		List<Courier> couriers = courierService.getCouriers(false);

		//Then
		assertThat(couriers)
			.containsExactlyInAnyOrderElementsOf(TestUtils.listOf(
				Courier.builder().id(1L).name("John Carter").active(true).build(),
				Courier.builder().id(2L).name("Amy Carter").active(false).build()
			));
		verify(courierRepository, times(1)).findAll();

	}

	@Test
	void updateCourierIfExists_courierWithProvidedIdExists_recordUpdatedSuccessfully() {
		//Given
		Optional<CourierEntity> persistedCourier = Optional.of(
			CourierEntity.builder().id(1L).firstName("John").lastName("Carter").active(false).build());
		doReturn(persistedCourier).when(courierRepository).findById(1L);
		doAnswer(invocation -> invocation.getArgument(0)).when(courierRepository).saveAndFlush(any());

		CourierService courierService = new CourierService(courierRepository, new CourierMapperImpl());
		CourierDto courierToUpdate = CourierDto.builder().firstName("John").lastName("Carter").active(true).build();

		//When
		Courier courier = courierService.updateCourierIfExists(1L, courierToUpdate);

		//Then
		verify(courierRepository, times(1)).findById(1L);
		ArgumentCaptor<CourierEntity> captor = ArgumentCaptor.forClass(CourierEntity.class);
		verify(courierRepository, times(1)).saveAndFlush(captor.capture());

		CourierEntity courierToBeSaved = CourierEntity.builder().id(1L).firstName("John").lastName("Carter")
			.active(true).build();
		assertThat(captor.getValue()).isEqualTo(courierToBeSaved);
		assertThat(courier).isEqualTo(Courier.builder().id(1L).name("John Carter").active(true).build());
	}

	@Test
	void updateCourierIfExists_courierWithProvidedIdIsAbsent_exceptionIsThrown() {
		//Given
		doReturn(Optional.empty()).when(courierRepository).findById(10L);
		CourierService courierService = new CourierService(courierRepository, new CourierMapperImpl());

		//When & Then
		Assertions.assertThatThrownBy(() -> courierService.updateCourierIfExists(10L, Mockito.mock(CourierDto.class)))
			.isInstanceOf(CourierNotFoundException.class)
			.hasMessage("Courier with id [10] not found");
	}
}
