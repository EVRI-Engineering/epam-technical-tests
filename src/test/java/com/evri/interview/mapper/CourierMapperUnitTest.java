package com.evri.interview.mapper;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.evri.interview.controller.dto.request.CourierDto;
import com.evri.interview.model.Courier;
import com.evri.interview.repository.CourierEntity;
import org.junit.jupiter.api.Test;

class CourierMapperUnitTest {

	@Test
	void entityToModel_validInput_correctResult() {
		CourierMapperImpl mapper = new CourierMapperImpl();

		assertThat(mapper.entityToModel(
			CourierEntity.builder().id(1L).firstName("John").lastName("Carter").active(true).build()))
			.isEqualTo(Courier.builder().id(1L).name("John Carter").active(true).build());
	}

	@Test
	void dtoToEntity_validInput_correctResult() {
		CourierMapperImpl mapper = new CourierMapperImpl();

		assertThat(mapper.dtoToEntity(
			CourierDto.builder().firstName("John").lastName("Carter").active(true).build(), 1L))
			.isEqualTo(CourierEntity.builder().id(1L).firstName("John").lastName("Carter").active(true).build());
	}
}
