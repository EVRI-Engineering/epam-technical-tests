package com.evri.interview.controller;

import com.evri.interview.exception.EntityNotFoundException;
import com.evri.interview.model.Courier;
import com.evri.interview.service.CourierService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@WebMvcTest
class CourierControllerTest {
    private static final Courier inactiveCourier = new Courier()
            .setName("Inactive Courier")
            .setActive(false);

    private static final Courier activeCourier = new Courier()
            .setName("Active Courier")
            .setActive(true);

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CourierService courierService;

    @Test
    public void getAllCouriers() throws Exception {
        when(courierService.getCouriers(false))
                .thenReturn(
                        Arrays.asList(inactiveCourier, activeCourier));

        mvc.perform(get("/api/couriers"))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$[0].name", is("Inactive Courier")),
                        jsonPath("$[0].active", is(false)),
                        jsonPath("$[1].name", is("Active Courier")),
                        jsonPath("$[1].active", is(true)),
                        jsonPath("$", hasSize(2))
                );
    }

    @Test
    public void getAllCouriersWithIsActiveTrue() throws Exception {
        when(courierService.getCouriers(true))
                .thenReturn(
                        Arrays.asList(activeCourier));

        mvc.perform(get("/api/couriers?isActive=true"))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$[0].name", is("Active Courier")),
                        jsonPath("$[0].active", is(true)),
                        jsonPath("$", hasSize(1))
                );
    }

    @Test
    public void updateCourier() throws Exception {
        when(courierService.updateCourier(1L, inactiveCourier))
                .thenReturn(inactiveCourier);

        mvc.perform(put("/api/couriers/1")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inactiveCourier)))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("name", is("Inactive Courier")),
                        jsonPath("active", is(false))
                );
    }

    @Test
    public void updateCourierWhenCourierNotFound() throws Exception {
        when(courierService.updateCourier(any(), any()))
                .thenThrow(new EntityNotFoundException("Not found"));

        mvc.perform(put("/api/couriers/1")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inactiveCourier)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateCourierWithInvalidName() throws Exception {
        Courier courier = new Courier()
                .setName("Name")
                .setActive(false);

        mvc.perform(put("/api/couriers/1")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(courier)))
                .andExpect(status().isBadRequest());
    }
}