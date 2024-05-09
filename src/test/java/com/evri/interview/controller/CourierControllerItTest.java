package com.evri.interview.controller;

import com.evri.interview.model.SaveCourier;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;


import static java.lang.String.format;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ActiveProfiles({"test"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CourierControllerItTest {

    private final Integer ID = 1;
    private final String FIRST_NANE = "Unknown";
    private final String LAST_NANE = "Unknown";
    private final String FULL_NAME =
            format("%s %s", FIRST_NANE, LAST_NANE);

    private final ObjectMapper MAPPER = new ObjectMapper();


    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturn200IfTheCourierSuccessfullyUpdated() throws Exception {
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
        mockMvc.perform(put("/api/courier/{courierId}", ID + 1000)
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
        mockMvc.perform(get("/api/couriers")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("isActive", "true"))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$.[0].id", is(ID)))
                .andExpect(jsonPath("$.[0].name", is(FULL_NAME)))
                .andExpect(jsonPath("$.[0].active", is(true)));

    }

    private String getContent(Object obj) throws Exception {
        return MAPPER.writeValueAsString(obj);
    }

}
