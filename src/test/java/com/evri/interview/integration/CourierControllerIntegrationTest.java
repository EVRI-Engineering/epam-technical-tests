package com.evri.interview.integration;

import com.evri.interview.Application;
import com.evri.interview.dto.CourierResponseDto;
import com.evri.interview.dto.CourierUpdateDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
public class CourierControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Order(1)
    public void getAllCouriers() throws Exception {
        CourierResponseDto courierResponseDto = CourierResponseDto.builder().id(1L).name("Ben Askew").active(true).build();
        CourierResponseDto courierResponseDto2 = CourierResponseDto.builder().id(2L).name("Summer Hunter").active(false).build();
        List<CourierResponseDto> courierResponseDtoList = new ArrayList<>();
        courierResponseDtoList.add(courierResponseDto);
        courierResponseDtoList.add(courierResponseDto2);
        byte[] expectedResult = objectMapper.writeValueAsBytes(courierResponseDtoList);

        mvc.perform(get("/api/couriers"))
                .andExpect(status().isOk())
                .andExpect(content().bytes(expectedResult));
    }

    @Test
    @Order(2)
    public void getAllActiveCouriers() throws Exception {
        CourierResponseDto courierResponseDto = CourierResponseDto.builder().id(1L).name("Ben Askew").active(true).build();
        List<CourierResponseDto> courierResponseDtoList = new ArrayList<>();
        courierResponseDtoList.add(courierResponseDto);
        byte[] expectedResult = objectMapper.writeValueAsBytes(courierResponseDtoList);

        mvc.perform(get("/api/couriers?isActive=true"))
                .andExpect(status().isOk())
                .andExpect(content().bytes(expectedResult));
    }

    @Test
    @Order(3)
    void updateCourier() throws Exception {
        long courierId = 1;
        CourierResponseDto courierResponseDto = CourierResponseDto.builder().id(1L).name("Ben Askew").active(false).build();
        CourierUpdateDto courierUpdateDto = CourierUpdateDto.builder().firstName("Ben").lastName("Askew").active(false).build();

        byte[] expectedResult = objectMapper.writeValueAsBytes(courierResponseDto);

        mvc.perform(put("/api/couriers/" + courierId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(courierUpdateDto)))
                .andExpect(status().isOk())
                .andExpect(content().bytes(expectedResult));
    }

    @Test
    @Order(4)
    public void updateCourierNotFound() throws Exception {
        long courierId = 3;
        CourierUpdateDto courierUpdateDto = CourierUpdateDto.builder().firstName("Ben").lastName("Askew").active(false).build();

        mvc.perform(put("/api/couriers/" + courierId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(courierUpdateDto)))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorCode").value("notFound"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").value("Courier not found with id = " + courierId))
                .andReturn();
    }

    @Test
    @Order(5)
    public void testUpdateCourierFirstNameIsBlank() throws Exception {
        long courierId = 1;
        CourierUpdateDto courierUpdateDto = CourierUpdateDto.builder().lastName("Askew").active(false).build();
        mvc.perform(put("/api/couriers/" + courierId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(courierUpdateDto)))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorCode").value("incorrectRequestData"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").value("First name cannot be blank"))
                .andReturn();
    }

    @Test
    @Order(6)
    public void testUpdateCourierLastNameIsBlank() throws Exception {
        long courierId = 2;
        CourierUpdateDto courierUpdateDto = CourierUpdateDto.builder().firstName("Ben").active(false).build();
        mvc.perform(put("/api/couriers/" + courierId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(courierUpdateDto)))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorCode").value("incorrectRequestData"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").value("Last name cannot be blank"))
                .andReturn();
    }

    @Test
    @Order(7)
    public void testUpdateCourierActiveIsNotProvided() throws Exception {
        long courierId = 2;
        CourierUpdateDto courierUpdateDto = CourierUpdateDto.builder().firstName("Ben").lastName("Askew").build();
        mvc.perform(put("/api/couriers/" + courierId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(courierUpdateDto)))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorCode").value("incorrectRequestData"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").value("Active status must be provided"))
                .andReturn();
    }

}
