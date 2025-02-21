package com.evri.interview.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.evri.interview.dto.CourierDto;
import com.evri.interview.model.Courier;
import com.evri.interview.service.CourierService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

@WebMvcTest(CourierController.class)
class CourierControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourierService courierService;

    private Courier courier1;
    private Courier courier2;

    @BeforeEach
    void setUp() {
        courier1 = Courier.builder().id(1L).name("John Doe").active(true).build();
        courier2 = Courier.builder().id(2L).name("Jane Doe").active(false).build();
    }

    @Test
    void getCouriers_active() throws Exception {
        when(courierService.getAllCouriers(true)).thenReturn(Collections.singletonList(courier1));

        mockMvc.perform(get("/api/couriers")
                        .param("isActive", "true")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(courier1.getName()))
                .andExpect(jsonPath("$[0].active").value(courier1.isActive()));
    }

    @Test
    void getAllCouriers() throws Exception {
        when(courierService.getAllCouriers(false)).thenReturn(Arrays.asList(courier1, courier2));

        mockMvc.perform(get("/api/couriers")
                        .param("isActive", "false")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(courier1.getName()))
                .andExpect(jsonPath("$[0].active").value(courier1.isActive()))
                .andExpect(jsonPath("$[1].name").value(courier2.getName()))
                .andExpect(jsonPath("$[1].active").value(courier2.isActive()));
    }

    @Test
    void updateCourier_success() throws Exception {
        CourierDto courierDto = new CourierDto("John", "Smith", true);

        Courier updatedCourier = Courier.builder().id(1L).name("John Smith").active(true).build();

        when(courierService.updateCourier(1L, courierDto)).thenReturn(Optional.of(updatedCourier));

        mockMvc.perform(put("/api/couriers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"John\",\"lastName\":\"Smith\",\"active\":true}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(updatedCourier.getName()))
                .andExpect(jsonPath("$.active").value(updatedCourier.isActive()));
    }

    @Test
    void updateCourier_notFound() throws Exception {
        CourierDto courierDto = new CourierDto("John", "Smith", true);

        when(courierService.updateCourier(1L, courierDto)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/couriers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"John\",\"lastName\":\"Smith\",\"active\":true}"))
                .andExpect(status().isNotFound());
    }
}