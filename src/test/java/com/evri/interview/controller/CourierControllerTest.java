package com.evri.interview.controller;

import com.evri.interview.dto.CourierDto;
import com.evri.interview.exception.CourierNotFoundException;
import com.evri.interview.model.Courier;
import com.evri.interview.service.CourierService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class CourierControllerTest {

	@MockBean
	private CourierService courierService;

	@InjectMocks
	private CourierController courierController;

	@Autowired
	private MockMvc mockMvc;

	@Test
	void testGetAllCouriers() throws Exception {
		Courier courier1 = Courier.builder()
			.id(1L)
			.name("John Doe")
			.active(true)
			.build();
		Courier courier2 = Courier.builder()
			.id(2L)
			.name("Jane Doe")
			.active(false)
			.build();
		List<Courier> couriers = Arrays.asList(courier1, courier2);

		when(courierService.getAllCouriers(false)).thenReturn(couriers);

		mockMvc.perform(get("/api/couriers")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().json("[{'id':1,'name':'John Doe','active':true},{'id':2,'name':'Jane Doe','active':false}]"));
	}

	@Test
	void testUpdateCourier() throws Exception {
		long courierId = 1L;
		CourierDto courierDto = CourierDto.builder()
			.firstName("John")
			.lastName("Doe")
			.active(true)
			.build();
		Courier updatedCourier = Courier.builder()
			.id(courierId)
			.name("John Doe")
			.active(true)
			.build();

		when(courierService.updateCourier(courierId, courierDto)).thenReturn(updatedCourier);

		mockMvc.perform(put("/api/couriers/{courierId}", courierId)
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"firstName\":\"John\",\"lastName\":\"Doe\",\"active\":true}"))
			.andExpect(status().isOk())
			.andExpect(content().json("{'id':1,'name':'John Doe','active':true}"));
	}

	@Test
	void testUpdateCourierNotFound() throws Exception {
		long courierId = 1L;
		CourierDto courierDto = CourierDto.builder()
			.firstName("John")
			.lastName("Doe")
			.active(true)
			.build();

		when(courierService.updateCourier(courierId, courierDto)).thenThrow(new CourierNotFoundException("Courier not found"));

		mockMvc.perform(put("/api/couriers/{courierId}", courierId)
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"firstName\":\"John\",\"lastName\":\"Doe\",\"active\":true}"))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.message").value("Courier not found"));
	}
}