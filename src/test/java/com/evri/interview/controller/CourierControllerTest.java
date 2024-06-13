package com.evri.interview.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.evri.interview.model.Courier;
import com.evri.interview.service.CourierService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@WebMvcTest(CourierController.class)
class CourierControllerTest {

  public static final long COURIER_ID = 1L;
  @Autowired
  private MockMvc mockMvc;
  @MockBean
  private CourierService courierService;
  @Autowired
  ObjectMapper objectMapper;

  @Test
  void shouldUpdateExistedCourier() throws Exception {
    Courier courier = Courier.builder().id(COURIER_ID).name("Test Courier").active(true).build();
    log.info(objectMapper.writeValueAsString(courier));

    when(courierService.getCourierByID(anyLong())).thenReturn(Optional.of(courier));

    mockMvc.perform(put("/api/couriers/{courierId}", COURIER_ID)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(courier)))
      .andExpect(status().isOk());

    verify(courierService).getCourierByID(courier.getId());
    verify(courierService).saveCourier(courier);
  }

  @Test
  void shouldNotUpdateNotExistedCourier() throws Exception {
    Courier courier = Courier.builder().id(COURIER_ID).name("Test Courier").active(true).build();

    when(courierService.getCourierByID(anyLong())).thenReturn(Optional.empty());

    mockMvc.perform(put("/api/couriers/{courierId}", COURIER_ID)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(courier)))
      .andExpect(status().isNotFound());

    verify(courierService).getCourierByID(courier.getId());
    verify(courierService, times(0)).saveCourier(any(Courier.class));
  }

  @Test
  void shouldReturnOnlyActiveUsers() throws Exception {
    Courier courier = Courier.builder().id(20L).name("Full Name").active(true).build();
    when(courierService.getAllActiveCouriers()).thenReturn(List.of(courier));

    mockMvc.perform(get("/api/couriers?isActive=true")
        .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(content().json(objectMapper.writeValueAsString(List.of(courier))));

    verify(courierService).getAllActiveCouriers();
    verify(courierService, times(0)).getAllCouriers();
  }
}