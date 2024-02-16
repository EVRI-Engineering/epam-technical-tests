package com.evri.interview.functional;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import lombok.SneakyThrows;

public class UpdateCourierFunctionalTest extends FunctionalTest {

  @Test
  @SneakyThrows
  @Sql(
      statements = {
        "INSERT INTO couriers (ID, FST_NME, LST_NME, ACTV) VALUES (1, 'Ben', 'Askew', 0)",
        "INSERT INTO couriers (ID, FST_NME, LST_NME, ACTV) VALUES (2, 'Anna', 'Green', 1)"
      })
  @Sql(statements = "DELETE FROM couriers", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  public void testUpdateCourier_shouldUpdateCourierPropertiesAndReturnUpdatedCourier() {
    mockMvc
        .perform(
            MockMvcRequestBuilders.put("/api/couriers/1")
                .content("{\"firstName\": \"John\", \"lastName\": \"Doe\", \"active\": true}")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("John Doe"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.active").value(true));
  }

  @Test
  @SneakyThrows
  @Sql(
      statements = {
        "INSERT INTO couriers (ID, FST_NME, LST_NME, ACTV) VALUES (1, 'Ben', 'Askew', 0)",
        "INSERT INTO couriers (ID, FST_NME, LST_NME, ACTV) VALUES (2, 'Anna', 'Green', 1)"
      })
  @Sql(statements = "DELETE FROM couriers", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  public void testUpdateCourier_shouldReturn404WhenCourierNotFound() {
    mockMvc
        .perform(
            MockMvcRequestBuilders.put("/api/couriers/3")
                .content("{\"firstName\": \"John\", \"lastName\": \"Doe\", \"active\": true}")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isNotFound());
  }

  @Test
  @SneakyThrows
  public void testUpdateCourier_shouldReturn400WhenFirstNameIsNotProvided() {
    mockMvc
        .perform(
            MockMvcRequestBuilders.put("/api/couriers/3")
                .content("{\"firstName\": \"\", \"lastName\": \"Doe\", \"active\": true}")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }
}
