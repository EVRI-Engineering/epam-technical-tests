package com.evri.interview.controller;

import com.evri.interview.dto.CourierRequestDto;
import com.evri.interview.dto.CourierResponseDto;
import com.evri.interview.exception.RecordNotFoundException;
import com.evri.interview.service.CourierService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = CourierController.class)
class CourierControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CourierService courierService;

    @Test
    public void shouldReturnOkStatus_whenGetAllCouriers() throws Exception {
        CourierResponseDto dto = CourierResponseDto.builder()
                .id(1)
                .name("Jane Doe")
                .active(false)
                .build();
        when(courierService.getAllCouriers(false)).thenReturn(Collections.singletonList(dto));

        mockMvc.perform(get("/api/couriers")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(1L))
                .andExpect(jsonPath("$.[0].name").value("Jane Doe"))
                .andExpect(jsonPath("$.[0].active").value(false));

    }

    @Test
    public void shouldReturnOkStatus_whenGetActiveCouriers() throws Exception {
        CourierResponseDto dto = CourierResponseDto.builder()
                .id(1)
                .name("Jane Doe")
                .active(true)
                .build();
        when(courierService.getAllCouriers(true)).thenReturn(Collections.singletonList(dto));

        mockMvc.perform(get("/api/couriers")
                        .contentType("application/json")
                        .param("isActive", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(1L))
                .andExpect(jsonPath("$.[0].name").value("Jane Doe"))
                .andExpect(jsonPath("$.[0].active").value(true));

    }

    @Test
    public void shouldReturnOkStatusAndResponseBody_whenPutExistingCourier() throws Exception {
        CourierResponseDto responseDto = CourierResponseDto.builder()
                .id(1)
                .name("Jane Doe")
                .active(true)
                .build();

        CourierRequestDto requestDto = new CourierRequestDto();
        requestDto.setFirstName("Jane");
        requestDto.setLastName("Doe");
        requestDto.setActive(true);
        when(courierService.updateCourierById(1, requestDto)).thenReturn(responseDto);

        mockMvc.perform(put("/api/couriers/1")
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Jane Doe"))
                .andExpect(jsonPath("$.active").value(true));

    }

    @Test
    public void shouldReturn404StatusCode_whenRecordNotFoundIsThrown() throws Exception {
        CourierRequestDto requestDto = new CourierRequestDto();
        requestDto.setFirstName("Jane");
        requestDto.setLastName("Doe");
        requestDto.setActive(true);
        when(courierService.updateCourierById(7, requestDto)).thenThrow(new RecordNotFoundException());

        mockMvc.perform(put("/api/couriers/7")
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType("application/json"))
                .andExpect(status().isNotFound());

    }

    @Test
    public void shouldReturn400StatusCode_whenNameIsNotValid() throws Exception {
        CourierRequestDto requestDto = new CourierRequestDto();
        requestDto.setFirstName("777Jane");
        requestDto.setLastName("Doe");
        requestDto.setActive(true);

        mockMvc.perform(put("/api/couriers/1")
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType("application/json"))
                .andExpect(status().isBadRequest());

    }

    @Test
    public void shouldReturn400StatusCode_whenSurnameIsNotValid() throws Exception {
        CourierRequestDto requestDto = new CourierRequestDto();
        requestDto.setFirstName("Jane");
        requestDto.setLastName("777Doe");
        requestDto.setActive(true);

        mockMvc.perform(put("/api/couriers/1")
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType("application/json"))
                .andExpect(status().isBadRequest());

    }

    @Test
    public void shouldReturn400StatusCode_whenNameIsBlank() throws Exception {
        CourierRequestDto requestDto = new CourierRequestDto();
        requestDto.setFirstName("  ");
        requestDto.setLastName("Doe");
        requestDto.setActive(true);

        mockMvc.perform(put("/api/couriers/1")
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType("application/json"))
                .andExpect(status().isBadRequest());

    }

    @Test
    public void shouldReturn400StatusCode_whenSurnameIsBlank() throws Exception {
        CourierRequestDto requestDto = new CourierRequestDto();
        requestDto.setFirstName("Jane");
        requestDto.setLastName("   ");
        requestDto.setActive(true);

        mockMvc.perform(put("/api/couriers/1")
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType("application/json"))
                .andExpect(status().isBadRequest());

    }


}