package com.evri.interview;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.Objects;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.evri.interview.repository.CourierEntity;
import com.evri.interview.repository.CourierRepository;
import com.evri.interview.request.CourierRequestBody;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("ApplicationIntegrationTest")
@ActiveProfiles("test")
class ApplicationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CourierRepository courierRepository;

    @Test
    void testGetAllCouriers() throws Exception {
        boolean isActive = false;

        mockMvc.perform(get("/api/couriers")
                .param("isActive", String.valueOf(isActive)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name")
                        .value("Ben Askew"))
                .andExpect(jsonPath("$[1].name")
                        .value("Sam Jones")); 
    }

    @Test
    void testGetAllActiveCouriers() throws Exception {
        boolean isActive = true;

        List<CourierEntity> courierEntities = courierRepository.findAll();
        List<CourierEntity> courierEntitiesWithStatusActive = courierRepository.findAllByActiveTrue();

        assertEquals(2, courierEntities.size());
        assertEquals(1, courierEntitiesWithStatusActive.size());

        assertEquals("Ben", courierEntitiesWithStatusActive.get(0).getFirstName());
        assertEquals("Askew", courierEntitiesWithStatusActive.get(0).getLastName());

        mockMvc.perform(get("/api/couriers")
                .param("isActive", String.valueOf(isActive)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name")
                        .value("Ben Askew"));
    }

    @Test
    void testUpdateCourierById_Success() throws JsonProcessingException, Exception {
        long courierIdForUpdate = 1L;

        CourierRequestBody courierRequestBody = CourierRequestBody.builder()
                .firstName("Sam")
                .lastName("Stone")
                .active(false)
                .build();

        CourierEntity courierEntityFromDbWhichWillBeUpdated = courierRepository.findById(courierIdForUpdate)
                        .orElseThrow(null);

        assertNotEquals(courierRequestBody.getFirstName(), courierEntityFromDbWhichWillBeUpdated.getFirstName());
        assertNotEquals(courierRequestBody.getLastName(), courierEntityFromDbWhichWillBeUpdated.getLastName());
        assertNotEquals(courierRequestBody.isActive(), courierEntityFromDbWhichWillBeUpdated.isActive());
        
        mockMvc.perform(put("/api/couriers/{courierId}", courierIdForUpdate)
                .contentType(Objects.requireNonNull(MediaType.APPLICATION_JSON))
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(courierRequestBody))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id")
                        .value("1"))
                .andExpect(jsonPath("$.name")
                        .value("Sam Stone"));
    }

    @Test
    void testUpdateCourierById_CourierNotFound() throws JsonProcessingException, Exception {
        long notExistCourierId = 3L; // Only 2 clients are inserted into the database

        CourierRequestBody courierRequestBody = CourierRequestBody.builder()
                .firstName("Sam")
                .lastName("Stone")
                .active(false)
                .build();

        mockMvc.perform(put("/api/couriers/{courierId}", notExistCourierId)
                .contentType(Objects.requireNonNull(MediaType.APPLICATION_JSON))
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(courierRequestBody))))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value(String.format("Not found courier with courierId: %s", notExistCourierId)));
    }

    @Test
    void testUpdateCourierById_InvalidFirstNameFormat() throws JsonProcessingException, Exception {
        long courierIdForUpdate = 1L;

        CourierRequestBody courierRequestBody = CourierRequestBody.builder()
                .firstName("sam")
                .lastName("Stone")
                .active(false)
                .build();

        mockMvc.perform(put("/api/couriers/{courierId}", courierIdForUpdate)
                .contentType(Objects.requireNonNull(MediaType.APPLICATION_JSON))
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(courierRequestBody))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.firstName")
                        .value("Invalid firstName format"));
    }

    @Test
    void testUpdateCourierById_InvalidLastNameFormat() throws JsonProcessingException, Exception {
        long courierIdForUpdate = 1L;

        CourierRequestBody courierRequestBody = CourierRequestBody.builder()
                .firstName("Sam")
                .lastName("stone")
                .active(false)
                .build();

        mockMvc.perform(put("/api/couriers/{courierId}", courierIdForUpdate)
                .contentType(Objects.requireNonNull(MediaType.APPLICATION_JSON))
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(courierRequestBody))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.lastName")
                        .value("Invalid lastName format"));
    }
    
    @Test
    void testUpdateCourierById_FirstNameAndLastNameNotBlank() throws JsonProcessingException, Exception {
        long courierIdForUpdate = 1L;

        CourierRequestBody courierRequestBody = CourierRequestBody.builder()
                .active(false)
                .build();

        mockMvc.perform(put("/api/couriers/{courierId}", courierIdForUpdate)
                .contentType(Objects.requireNonNull(MediaType.APPLICATION_JSON))
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(courierRequestBody))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.firstName")
                        .value("firstName is required"))
                .andExpect(jsonPath("$.lastName")
                        .value("lastName is required"));
    }
}
