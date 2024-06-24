package com.evri.interview.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.evri.interview.exception.CourierNotFoundException;
import com.evri.interview.model.Courier;
import com.evri.interview.model.CourierPutDataModel;
import com.evri.interview.service.CourierService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;

@WebMvcTest(CourierController.class)
class CourierControllerTest {

    @Autowired
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @MockBean
    private CourierService courierService;

    @Test
    @SneakyThrows
    void shouldReturnCourier_WhenGetCalled() {

        when(courierService.getAllCouriers(false)).thenReturn(Arrays.asList(aCourier()));

        mockMvc.perform(get("/api/couriers"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.[0].id").value(1L))
            .andExpect(jsonPath("$.[0].name").value("Courier Foo"))
            .andExpect(jsonPath("$.[0].active").value(false));
    }

    @Test
    @SneakyThrows
    void shouldReturn404_WhenCourierForUpdateNotFound() {

        doThrow(CourierNotFoundException.class).when(courierService).updateCourierData(anyLong(), any());

        mockMvc.perform(put("/api/couriers/1").contentType("application/json").content(objectMapper.writeValueAsString(aCourierPutDataModel())))
            .andExpect(status().isNotFound());
    }

    private Courier aCourier() {

        return Courier.builder()
            .id(1L)
            .name("Courier Foo")
            .active(false)
            .build();
    }

    private CourierPutDataModel aCourierPutDataModel() {

        return new CourierPutDataModel("Courier", "Foo", false);
    }
}