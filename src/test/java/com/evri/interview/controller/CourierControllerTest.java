package com.evri.interview.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.evri.interview.model.Courier;
import com.evri.interview.service.CourierService;
import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest
class CourierControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private CourierService courierService;

  @Test
  public void getAllCouriersTest_Success() throws Exception {
    when(courierService.getAllCouriers(false)).thenReturn(
        Collections.singletonList(Courier.builder()
            .id(1L)
            .firstName("Andrew")
            .lastName("Jackson")
            .active(false)
            .build()));

    mockMvc.perform(MockMvcRequestBuilders.get("/api/couriers")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].firstName").value("Andrew"))
        .andExpect(jsonPath("$[0].lastName").value("Jackson"));
  }

  @Test
  public void updateCourierTest_Success() throws Exception {
    Courier updatedCourier = Courier.builder()
        .id(1L)
        .firstName("Valid")
        .lastName("User")
        .active(true)
        .build();
    when(courierService.updateCourier(eq(1L), any(Courier.class))).thenReturn(
        Optional.of(updatedCourier));

    String updatedCourierJson = "{\"id\":1,\"firstName\":\"Valid\",\"lastName\":\"User\",\"active\":true}";

    mockMvc.perform(MockMvcRequestBuilders.put("/api/couriers/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(updatedCourierJson))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.firstName").value("Valid"))
        .andExpect(jsonPath("$.lastName").value("User"))
        .andExpect(jsonPath("$.active").value(true));
  }

  @Test
  public void updateCourierTest_NotFound() throws Exception {
    Courier updatedCourier = Courier.builder()
        .id(99L)
        .firstName("Valid")
        .lastName("User")
        .active(true)
        .build();
    when(courierService.updateCourier(anyLong(), any(Courier.class))).thenReturn(
        Optional.empty());

    String updatedCourierJson = "{\"id\":99,\"firstName\":\"Invalid\",\"lastName\":\"User\",\"active\":true}";

    mockMvc.perform(MockMvcRequestBuilders.put("/api/couriers/99")
            .contentType(MediaType.APPLICATION_JSON)
            .content(updatedCourierJson))
        .andExpect(status().isNotFound());
  }
}
