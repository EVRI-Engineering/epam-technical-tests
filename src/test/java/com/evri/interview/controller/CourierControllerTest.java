package com.evri.interview.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.evri.interview.exception.CourierNotFound;
import com.evri.interview.model.FullCourierDto;
import com.evri.interview.model.ShortCourierDto;
import com.evri.interview.service.CourierService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CourierController.class)
class CourierControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private CourierService courierService;

  @Test
  public void whenIsActiveFalse_thenAllInActiveCouriersShouldBeReturned() throws Exception {
    when(courierService.getAllCouriers(Boolean.FALSE)).thenReturn(
        Arrays.asList(new ShortCourierDto(1, "Courier FullName1", Boolean.FALSE),
            new ShortCourierDto(2, "Courier FullName2", Boolean.FALSE)));

    mockMvc.perform(get("/api/couriers")
            .param("isActive", "false"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$[0].id", is(1)))
        .andExpect(jsonPath("$[1].id", is(2)));
  }

  @Test
  public void whenIsActiveTrue_thenAllActiveCouriersShouldBeReturned() throws Exception {
    when(courierService.getAllCouriers(Boolean.TRUE)).thenReturn(
        Arrays.asList(new ShortCourierDto(1, "Courier FullName1", Boolean.TRUE)));

    mockMvc.perform(get("/api/couriers")
            .param("isActive", "true"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$[0].id", is(1)));
  }

  @Test
  public void whenCouriersNewAttributesAreSet_thenAppropriateCourierAttributesHaveToBeUpdated()
      throws Exception {
    FullCourierDto request = new FullCourierDto("Volodymyr", "Senkiv_Updated",
        false);
    ShortCourierDto response = new ShortCourierDto(1, "Volodymyr Senkiv_Updated",
        false);

    when(courierService.updateCourier(1, request)).thenReturn(response);

    mockMvc.perform(put("/api/couriers/1")
            .content(new ObjectMapper().writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id", is(1)))
        .andExpect(jsonPath("$.name", is("Volodymyr Senkiv_Updated")));

  }

  @Test
  public void whenCourierDoesntExist_then404NotFoundHasToBeReturned()
      throws Exception {
    FullCourierDto request = new FullCourierDto("Volodymyr", "Senkiv_Updated",
        false);
    when(courierService.updateCourier(9999, request)).thenThrow(CourierNotFound.class);

    mockMvc.perform(put("/api/couriers/9999")
            .content(new ObjectMapper().writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }
}