package com.evri.interview.controller;

import com.evri.interview.model.Courier;
import com.evri.interview.service.CourierService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CourierControllerTest {

  @Mock
  private CourierService courierService;

  private MockMvc mockMvc;

  @BeforeEach
  void setUp() {
    final CourierController courierController = new CourierController(courierService);
    mockMvc = MockMvcBuilders
        .standaloneSetup(courierController)
        .build();
  }

  @Test
  void shouldReturnAllCouriersWhenRequestParamIsAbsent() throws Exception {
    mockMvc.perform(
            MockMvcRequestBuilders.get("http://localhost:8080/api/couriers")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn();

    verify(courierService).getAllCouriers();
    verify(courierService, times(0)).getAllActiveCouriers();
  }

  @Test
  void shouldReturnAllCouriersWhenRequestParamIsFalse() throws Exception {
    mockMvc.perform(
            MockMvcRequestBuilders.get("http://localhost:8080/api/couriers?isActive=false")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn();

    verify(courierService).getAllCouriers();
    verify(courierService, times(0)).getAllActiveCouriers();
  }

  @Test
  void shouldReturnActiveCouriersWhenRequestParamIsTrue() throws Exception {
    mockMvc.perform(
            MockMvcRequestBuilders.get("http://localhost:8080/api/couriers?isActive=true")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn();

    verify(courierService, times(0)).getAllCouriers();
    verify(courierService).getAllActiveCouriers();
  }

  @Test
  void shouldReturnOkStatusWhenCourierWasUpdated() throws Exception {
    when(courierService.updateCourierById(eq(1L), any()))
        .thenReturn(Courier.builder().id(1).name("John Doe").active(false).build());

    mockMvc.perform(
            MockMvcRequestBuilders.put("http://localhost:8080/api/couriers/1")
                .content(courierToUpdate())
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn();

    verify(courierService).updateCourierById(eq(1L), any());
  }

  @Test
  void shouldReturnNotFoundStatusWhenExceptionOccurred() throws Exception {
    when(courierService.updateCourierById(eq(1L), any()))
        .thenThrow(new IllegalArgumentException());

    mockMvc.perform(
            MockMvcRequestBuilders.put("http://localhost:8080/api/couriers/1")
                .content(courierToUpdate())
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andReturn();

    verify(courierService).updateCourierById(eq(1L), any());
  }

  private String courierToUpdate() {
    return """
        {
          "firstName": "John",
          "lastName": "Doe",
          "active": "false"
        }
        """;
  }

}
