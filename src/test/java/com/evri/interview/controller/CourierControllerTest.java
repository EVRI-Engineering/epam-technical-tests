package com.evri.interview.controller;

import com.evri.interview.exception.EntityNotFoundException;
import com.evri.interview.model.Courier;
import com.evri.interview.service.CourierService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CourierController.class)
public class CourierControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourierService courierService;

    @Test
    @SneakyThrows
    void shouldReturnCourier_WhenGetCalledWithTrueFlag() {

        when(courierService.getAllCouriers(true))
                .thenReturn(Lists.newArrayList(createCourier(true),
                        createCourier(false)));

        mockMvc.perform(get("/api/couriers"))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    void shouldReturnCourier_WhenGetCalledWithFalseFlag() {

        when(courierService.getAllCouriers(false))
                .thenReturn(Lists.newArrayList(createCourier(true),
                        createCourier(false)));

        mockMvc.perform(get("/api/couriers?isOnlyActive=true"))
                .andExpect(status().isOk());
    }


    @Test
    @SneakyThrows
    void shouldReturn404_WhenCourierForUpdateNotFound() {

        doThrow(EntityNotFoundException.class).when(courierService).updateCourier(anyLong(), any());

        mockMvc.perform(put("/api/couriers/{id}", 11L)
                        .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(createCourier(true))))
                .andExpect(status().isNotFound());
    }

    @Test
    @SneakyThrows
    void shouldRUpdateCourier() {

        mockMvc.perform(put("/api/couriers/{id}", 10L)
                        .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(createCourier(true))))
                .andExpect(status().isOk());
    }

    private Courier createCourier(boolean active) {

        return Courier.builder()
                .id(10L)
                .name("Alex Sidorov")
                .active(active)
                .build();
    }


}
