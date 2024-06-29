package com.evri.interview.controller;

import static org.assertj.core.api.Assertions.assertThatCollection;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.evri.interview.controller.dto.request.CourierDto;
import com.evri.interview.model.Courier;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class CourierControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	public void getAllCouriers_isActiveIsTrue_onlyActiveAreReturned() throws Exception {
		MvcResult result = mockMvc.perform(get("/api/v1/couriers")
				.param("isActive", "true")
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andReturn();

		String jsonResponse = result.getResponse().getContentAsString();
		List<Courier> couriers = objectMapper.readValue(jsonResponse, new TypeReference<List<Courier>>() {});

		assertThatCollection(couriers)
			.hasSize(1)
			.containsExactly(Courier.builder().id(1L).name("Ben Askew").active(true).build());
	}

	@Test
	public void getAllCouriers_isActiveIsNotDefined_allAreReturned() throws Exception {
		MvcResult result = mockMvc.perform(get("/api/v1/couriers")
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andReturn();

		String jsonResponse = result.getResponse().getContentAsString();
		List<Courier> couriers = objectMapper.readValue(jsonResponse, new TypeReference<List<Courier>>() {});

		assertThatCollection(couriers)
			.hasSize(3)
			.containsExactlyInAnyOrder(
				Courier.builder().id(1L).name("Ben Askew").active(true).build(),
				Courier.builder().id(2L).name("John Carter").active(false).build(),
				Courier.builder().id(3L).name("Amy Carter").active(false).build()
			);
	}

	@Test
	public void updateCourierIfExists_courierIsPersisted_updatedSuccessfully() throws Exception {
		CourierDto courierDto = CourierDto.builder().firstName("John").lastName("Carter").active(true).build();
		String courierUpdateJson = objectMapper.writeValueAsString(courierDto);

		MvcResult result = mockMvc.perform(put("/api/v1/couriers/2")
				.contentType(MediaType.APPLICATION_JSON)
				.content(courierUpdateJson))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andReturn();

		String jsonResponse = result.getResponse().getContentAsString();
		Courier updatedCourier = objectMapper.readValue(jsonResponse, Courier.class);

		assertThat(updatedCourier).isEqualTo(
			Courier.builder().id(2L).name("John Carter").active(true).build()
		);
	}

	@Test
	public void testUpdateCourierNotFound() throws Exception {
		CourierDto courierDto = CourierDto.builder().firstName("John").lastName("Carter").active(true).build();
		String courierUpdateJson = objectMapper.writeValueAsString(courierDto);

		mockMvc.perform(put("/api/v1/couriers/9999")
				.contentType(MediaType.APPLICATION_JSON)
				.content(courierUpdateJson))
			.andExpect(status().isNotFound());
	}
}
