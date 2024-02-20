package com.evri.interview;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.evri.interview.exception.ResourceNotFoundException;
import com.evri.interview.model.Courier;
import com.evri.interview.request.CourierRequestBody;
import com.evri.interview.service.CourierService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("ApplicationIntegrationTest")
class ApplicationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CourierService courierService;

    @Test
    void testGetAllCouriers() throws Exception {
        boolean isActive = false;

        List<Courier> mockCourierList = Arrays.asList(
                Courier.builder().id(1).name("Dan Garcia").active(false).build(),
                Courier.builder().id(2).name("Alice Smith").active(true).build()
        );

        when(courierService.getAllCouriers(isActive)).thenReturn(mockCourierList);

        mockMvc.perform(get("/api/couriers")
                .param("isActive", String.valueOf(isActive)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Dan Garcia"))
                .andExpect(jsonPath("$[1].name").value("Alice Smith")); 
                
        verify(courierService, times(1)).getAllCouriers(false);
    }

    @Test
    void testUpdateCourierById_Success() throws JsonProcessingException, Exception {
        long courierIdForUpdate = 1L;

        Courier updatedCourier = Courier.builder().id(courierIdForUpdate).name("Sam Stone").build();

        CourierRequestBody mockCourierRequestBody = CourierRequestBody.builder()
                .firstName("Sam")
                .lastName("Stone")
                .active(false)
                .build();

        when(courierService.updateCourierById(eq(courierIdForUpdate), any(CourierRequestBody.class))).thenReturn(updatedCourier);

        mockMvc.perform(put("/api/couriers/{courierId}", courierIdForUpdate)
                .contentType(Objects.requireNonNull(MediaType.APPLICATION_JSON))
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(mockCourierRequestBody))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Sam Stone"));

        verify(courierService, times(1))
                .updateCourierById(courierIdForUpdate, mockCourierRequestBody);
    }

    @Test
    void testUpdateCourierById_CourierNotFound() throws JsonProcessingException, Exception {
        long notExistCourierId = 1000L;

        CourierRequestBody mockCourierRequestBody = CourierRequestBody.builder()
                .firstName("Sam")
                .lastName("Stone")
                .active(false)
                .build();

        when(courierService.updateCourierById(eq(notExistCourierId), any(CourierRequestBody.class)))
                .thenThrow(
                    new ResourceNotFoundException(String.format("Not found courier with courierId: %s", notExistCourierId)
                )
        );

        mockMvc.perform(put("/api/couriers/{courierId}", notExistCourierId)
                .contentType(Objects.requireNonNull(MediaType.APPLICATION_JSON))
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(mockCourierRequestBody))))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value(String.format("Not found courier with courierId: %s", notExistCourierId)));

        verify(courierService, times(1))
                .updateCourierById(notExistCourierId, mockCourierRequestBody);

    }
}
