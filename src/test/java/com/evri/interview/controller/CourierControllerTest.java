package com.evri.interview.controller;

import com.evri.interview.dto.CourierResponseDto;
import com.evri.interview.dto.CourierUpdateDto;
import com.evri.interview.exception.EntityNotFoundException;
import com.evri.interview.service.CourierService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@ExtendWith(SpringExtension.class)
public class CourierControllerTest {

    @MockBean
    private CourierService courierService;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    public void testGetAllActiveCouriers() throws Exception {
        boolean isActive = true;
        CourierResponseDto courierResponseDto = CourierResponseDto.builder().id(1).name("Joyce Barton").active(true).build();
        List<CourierResponseDto> courierResponseDtoList = new ArrayList<>();
        courierResponseDtoList.add(courierResponseDto);
        byte[] expectedResult = objectMapper.writeValueAsBytes(courierResponseDtoList);

        when(courierService.getAllCouriers(isActive)).thenReturn(courierResponseDtoList);

        mvc.perform(get("/api/couriers?isActive=" + isActive))
                .andExpect(status().isOk())
                .andExpect(content().bytes(expectedResult));
    }

    @Test
    public void testGetAllCouriers() throws Exception {
        boolean isActive = false;
        CourierResponseDto courierResponseDto = CourierResponseDto.builder().id(1).name("Joyce Barton").active(false).build();
        CourierResponseDto courierResponseDto2 = CourierResponseDto.builder().id(2).name("Summer Hunter").active(false).build();
        List<CourierResponseDto> courierResponseDtoList = new ArrayList<>();
        courierResponseDtoList.add(courierResponseDto);
        courierResponseDtoList.add(courierResponseDto2);
        byte[] expectedResult = objectMapper.writeValueAsBytes(courierResponseDtoList);

        when(courierService.getAllCouriers(isActive)).thenReturn(courierResponseDtoList);

        mvc.perform(get("/api/couriers"))
                .andExpect(status().isOk())
                .andExpect(content().bytes(expectedResult));
    }

    @Test
    public void testUpdateCourier() throws Exception {
        long courierId = 2;
        CourierUpdateDto courierUpdateDto = CourierUpdateDto.builder().firstName("Joyce").lastName("Barton").active(true).build();
        CourierResponseDto courierResponseDto = CourierResponseDto.builder().id(courierId).name("Joyce Barton").active(false).build();

        byte[] expectedResult = objectMapper.writeValueAsBytes(courierResponseDto);

        when(courierService.updateCourierById(eq(courierId), any())).thenReturn(courierResponseDto);

        mvc.perform(put("/api/couriers/" + courierId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(courierUpdateDto)))
                .andExpect(status().isOk())
                .andExpect(content().bytes(expectedResult));
    }

    @Test
    public void testUpdateCourierNotFound() throws Exception {
        long courierId = 2;
        CourierUpdateDto courierUpdateDto = CourierUpdateDto.builder().firstName("Joyce").lastName("Barton").active(true).build();

        when(courierService.updateCourierById(eq(courierId), any())).thenThrow(EntityNotFoundException.class);

        mvc.perform(put("/api/couriers/" + courierId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(courierUpdateDto)))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void testUpdateCourierFirstNameIsBlank() throws Exception {
        long courierId = 2;
        CourierUpdateDto courierUpdateDto = CourierUpdateDto.builder().firstName("").lastName("Barton").active(true).build();
        mvc.perform(put("/api/couriers/" + courierId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(courierUpdateDto)))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void testUpdateCourierLastNameIsBlank() throws Exception {
        long courierId = 2;
        CourierUpdateDto courierUpdateDto = CourierUpdateDto.builder().firstName("Joyce").active(true).build();
        mvc.perform(put("/api/couriers/" + courierId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(courierUpdateDto)))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void testUpdateCourierActiveIsNotProvided() throws Exception {
        long courierId = 2;
        CourierUpdateDto courierUpdateDto = CourierUpdateDto.builder().firstName("Joyce").lastName("Barton").build();
        mvc.perform(put("/api/couriers/" + courierId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(courierUpdateDto)))
                .andExpect(status().isBadRequest())
                .andReturn();
    }
}
