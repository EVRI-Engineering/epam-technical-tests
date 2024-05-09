package com.evri.interview.controller;


import com.evri.interview.model.Courier;
import com.evri.interview.model.SaveCourier;
import com.evri.interview.service.CourierService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.lang.String.format;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class CourierControllerTest {

    private final Integer ID = 1;
    private final String FIRST_NANE = "Unknown";
    private final String LAST_NANE = "Unknown";
    private final String FULL_NAME =
            format("%s %s", FIRST_NANE, LAST_NANE);


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourierService courierService;

    @Test
    void shouldReturn200IfTheCourierSuccessfullyUpdated() throws Exception {
        // Given
        Optional<Courier> courier = Optional.of(
                Courier.builder()
                        .id(ID)
                        .name(FULL_NAME)
                        .active(true)
                        .build()
        );

        // When
        when(courierService.updateCourier(
                anyLong(), any(SaveCourier.class))).thenReturn(courier);

        // Then
        mockMvc.perform(put("/api/courier/{courierId}", ID)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                getContent(
                                        SaveCourier.builder()
                                                .firstName(FIRST_NANE)
                                                .lastName(LAST_NANE)
                                                .active(true)
                                                .build()
                                )
                        )
                )
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.id", is(ID)))
                .andExpect(jsonPath("$.name", is(FULL_NAME)))
                .andExpect(jsonPath("$.active", is(true)));

    }

    @Test
    void shouldReturn404IfTheCourierIsNotFound() throws Exception {
        // Given
        Optional<Courier> courier = Optional.empty();

        // When
        when(courierService.updateCourier(
                anyLong(), any(SaveCourier.class))).thenReturn(courier);

        // Then
        mockMvc.perform(put("/api/courier/{courierId}", ID)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                getContent(
                                        SaveCourier.builder()
                                                .firstName(FIRST_NANE)
                                                .lastName(LAST_NANE)
                                                .active(true)
                                                .build()
                                )
                        )
                )
                .andExpect(status().is(HttpStatus.NOT_FOUND.value()));

    }

    @Test
    void shouldSuccessfullyReturnListOfAllCouriers() throws Exception {
        // Given
        List<Courier> couriers = Arrays.asList(
                Courier.builder()
                        .id(ID)
                        .name(FULL_NAME)
                        .active(true)
                        .build(),
                Courier.builder()
                        .id(ID + 1)
                        .name(FULL_NAME)
                        .active(false)
                        .build()
                );

        // When
        when(courierService.getAllCouriers(false)).thenReturn(couriers);

        // Then
        mockMvc.perform(get("/api/couriers")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.[0].id", is(ID)))
                .andExpect(jsonPath("$.[0].name", is(FULL_NAME)))
                .andExpect(jsonPath("$.[0].active", is(true)))
                .andExpect(jsonPath("$.[1].id", is(ID + 1)))
                .andExpect(jsonPath("$.[1].name", is(FULL_NAME)))
                .andExpect(jsonPath("$.[1].active", is(false)));;

    }

    @Test
    void shouldSuccessfullyReturnOnlyActiveCouriers() throws Exception {
        // Given
        List<Courier> couriers = Arrays.asList(
                        Courier.builder()
                        .id(ID)
                        .name(FULL_NAME)
                        .active(true)
                        .build()
        );

        // When
        when(courierService.getAllCouriers(true)).thenReturn(couriers);

        // Then
        mockMvc.perform(get("/api/couriers")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("isActive", "true"))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$.[0].id", is(ID)))
                .andExpect(jsonPath("$.[0].name", is(FULL_NAME)))
                .andExpect(jsonPath("$.[0].active", is(true)));;

    }



    private String getContent(Object obj) throws Exception {
        return new ObjectMapper().writeValueAsString(obj);
    }

}
