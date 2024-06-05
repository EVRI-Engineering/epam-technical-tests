package com.evri.interview.controller;

import com.evri.interview.exceptions.ResourceNotFoundException;
import com.evri.interview.model.Courier;
import com.evri.interview.model.ErrorResponse;
import com.evri.interview.model.UpdateCourierData;
import com.evri.interview.service.CourierService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(CourierController.class)
class CourierControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourierService courierService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllCouriersProvidedIsActiveFlagFalse() throws Exception {
        List<Courier> expectedCouriers = Arrays.asList(
            Courier.builder().id(1).name("John Snow").active(true).build(),
            Courier.builder().id(2).name("Rob Stark").active(false).build()
        );
        when(courierService.getAllCouriersByActivity(anyBoolean())).thenReturn(expectedCouriers);
        String expectedJson = objectMapper.writeValueAsString(expectedCouriers);

        mockMvc.perform(get("/api/couriers?isActive=false"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json(expectedJson));

        verify(courierService).getAllCouriersByActivity(false);
    }

    @Test
    void getAllCouriersProvidedIsActiveFlagTrue() throws Exception {
        List<Courier> expectedCouriers = Arrays.asList(
                Courier.builder().id(1).name("John Snow").active(true).build(),
                Courier.builder().id(2).name("Rob Stark").active(false).build()
        );
        when(courierService.getAllCouriersByActivity(anyBoolean())).thenReturn(expectedCouriers);
        String expectedJson = objectMapper.writeValueAsString(expectedCouriers);

        mockMvc.perform(get("/api/couriers?isActive=true"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json(expectedJson));

        verify(courierService).getAllCouriersByActivity(true);
    }

    @Test
    void getAllCouriersNotProvidedIsActiveFlag() throws Exception {
        List<Courier> expectedCouriers = Arrays.asList(
                Courier.builder().id(1).name("John Snow").active(true).build(),
                Courier.builder().id(2).name("Rob Stark").active(false).build()
        );
        when(courierService.getAllCouriers()).thenReturn(expectedCouriers);
        String expectedJson = objectMapper.writeValueAsString(expectedCouriers);

        mockMvc.perform(get("/api/couriers"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json(expectedJson));

        verify(courierService).getAllCouriers();
    }

    @Test
    void updateByIdExistingCourier() throws Exception {
        Courier expectedCourier = Courier.builder().id(1).name("John Snow")
                .active(true).build();
        UpdateCourierData updateData = UpdateCourierData.builder().firstName("ModifiedFirstName")
                .lastName("ModifiedLastName")
                .isActive(false).build();

        when(courierService.updateById(anyLong(), any())).thenReturn(expectedCourier);
        String expectedJson = objectMapper.writeValueAsString(expectedCourier);
        String updateDataJson = objectMapper.writeValueAsString(updateData);

        mockMvc.perform(put("/api/couriers/" + expectedCourier.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateDataJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json(expectedJson));
        verify(courierService).updateById(expectedCourier.getId(), updateData);
    }

    @Test
    void updateByIdNonExistingCourier() throws Exception {
        UpdateCourierData updateData = UpdateCourierData.builder().firstName("ModifiedFirstName")
                .lastName("ModifiedLastName")
                .isActive(false).build();
        String expectedErrorMessage = "error message";
        ErrorResponse expectedErrorResponse = new ErrorResponse(expectedErrorMessage);

        when(courierService.updateById(anyLong(), any())).thenThrow(new ResourceNotFoundException(expectedErrorMessage));
        String updateDataJson = objectMapper.writeValueAsString(updateData);
        String expectedResponseJson = objectMapper.writeValueAsString(expectedErrorResponse);

        mockMvc.perform(put("/api/couriers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateDataJson))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json(expectedResponseJson));
        verify(courierService).updateById(1, updateData);
    }

    @Test
    void updateByIdFirstNameIsBlank() throws Exception {
        UpdateCourierData updateData = UpdateCourierData.builder().firstName(" ")
                .lastName("lastName")
                .isActive(false).build();

        String updateDataJson = objectMapper.writeValueAsString(updateData);

        mockMvc.perform(put("/api/couriers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateDataJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.firstName").value("First name is mandatory"));
        verify(courierService, never()).updateById(1, updateData);
    }

    @Test
    void updateByIdLastNameIsBlank() throws Exception {
        UpdateCourierData updateData = UpdateCourierData.builder().firstName("lastName")
                .lastName(" ")
                .isActive(false).build();

        String updateDataJson = objectMapper.writeValueAsString(updateData);

        mockMvc.perform(put("/api/couriers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateDataJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.lastName").value("Last Name is mandatory"));
        verify(courierService, never()).updateById(1, updateData);
    }

    @Test
    void updateByIdFirstNameNotProvided() throws Exception {
        UpdateCourierData updateData = UpdateCourierData.builder()
                .lastName("lastName")
                .isActive(false).build();

        String updateDataJson = objectMapper.writeValueAsString(updateData);

        mockMvc.perform(put("/api/couriers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateDataJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.firstName").value("First name is mandatory"));
        verify(courierService, never()).updateById(1, updateData);
    }

    @Test
    void updateByIdLastNameNotProvided() throws Exception {
        UpdateCourierData updateData = UpdateCourierData.builder().firstName("lastName")
                .isActive(false).build();

        String updateDataJson = objectMapper.writeValueAsString(updateData);

        mockMvc.perform(put("/api/couriers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateDataJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.lastName").value("Last Name is mandatory"));
        verify(courierService, never()).updateById(1, updateData);
    }

    @Test
    void updateByIdIsActiveNotProvided() throws Exception {
        UpdateCourierData updateData = UpdateCourierData.builder().firstName("lastName").
                lastName("lastName").build();

        String updateDataJson = objectMapper.writeValueAsString(updateData);

        mockMvc.perform(put("/api/couriers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateDataJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.isActive").value("must not be null"));
        verify(courierService, never()).updateById(1, updateData);
    }
}
