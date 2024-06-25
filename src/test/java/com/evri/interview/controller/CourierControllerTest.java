package com.evri.interview.controller;

import com.evri.interview.model.Courier;
import com.evri.interview.model.CourierDTO;
import com.evri.interview.service.CourierService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(CourierController.class)
class CourierControllerTest {
    ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    CourierService courierService;
    @InjectMocks
    private CourierController courierController;

    @Test
    void getAllCouriersReturnsActiveCouriersWhenActiveParamIsTrue() throws Exception {
        List<Courier> couriers = Arrays.asList(
                Courier.builder().id(13).name("name11").active(true).build(),
                Courier.builder().id(10).name("name22  name").active(true).build());

        when(courierService.getAllCouriersWithActiveStatus(true)).thenReturn(couriers);

        this.mockMvc.perform(get("/api/couriers/").param("isActive", "true"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(couriers)));
    }

    @Test
    void getAllCouriersUsesDefaultActiveFalse() throws Exception {
        List<Courier> couriers = Arrays.asList(
                Courier.builder().id(1).name("name1").active(false).build(),
                Courier.builder().id(12).name("name22").active(false).build());

        when(courierService.getAllCouriersWithActiveStatus(false)).thenReturn(couriers);

        this.mockMvc.perform(get("/api/couriers/"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(couriers)));
    }

    @Test
    void updateCourierReturns200AndDtoWhenSuccessfullyUpdated() throws Exception {
        final long id = 2;
        final CourierDTO courierDTO = CourierDTO.builder()
                .firstName("testName")
                .lastName("testSurname")
                .active(true)
                .build();
        final String courierAsJSON = objectMapper.writeValueAsString(courierDTO);

        when(courierService.updateCourier(id, courierDTO)).thenReturn(Optional.of(courierDTO));

        this.mockMvc.perform(put("/api/couriers/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(courierAsJSON))
                .andExpect(status().isOk())
                .andExpect(content().json(courierAsJSON));
    }

    @Test
    void updateCourierReturns404WhenCourierNotExists() throws Exception {
        final long id = 3;
        final CourierDTO courierDTO = CourierDTO.builder()
                .firstName("testName")
                .lastName("testSurname")
                .active(true)
                .build();
        final String courierAsJSON = objectMapper.writeValueAsString(courierDTO);

        when(courierService.updateCourier(id, courierDTO)).thenReturn(Optional.empty());

        this.mockMvc.perform(put("/api/couriers/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(courierAsJSON))
                .andExpect(status().is(404));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "{\"firstName\":\"  \",\"lastName\":\"testSurname\",\"active\":true}",
            "{\"lastName\":\"testSurname\",\"active\":true}",
            "{\"firstName\":\"name\",\"lastName\":\"\",\"active\":true}",
            "{\"firstName\":\"name\",\"lastName\":\"testSurname\"}"
    })
    void updateCourierReturns400WhenParameterMissingOrBlank(String input) throws Exception {
        final long id = 3;

        this.mockMvc.perform(put("/api/couriers/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(input))
                .andExpect(status().is(400));
    }
}
