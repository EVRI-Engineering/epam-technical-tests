package com.evri.interview.functional;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import lombok.SneakyThrows;

public class GetAllCouriersFunctionalTest extends FunctionalTest {

  @Test
  @SneakyThrows
  @Sql(
      statements = {
        "INSERT INTO couriers (ID, FST_NME, LST_NME, ACTV) VALUES (1, 'Ben', 'Askew', 1)",
        "INSERT INTO couriers (ID, FST_NME, LST_NME, ACTV) VALUES (2, 'Anna', 'Green', 0)",
        "INSERT INTO couriers (ID, FST_NME, LST_NME, ACTV) VALUES (3, 'Harry', 'Scott', 1)"
      })
  @Sql(statements = "DELETE FROM couriers", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  public void testGetAllCouriers_shouldReturnAllCouriersWhenIsActiveIsNotPassed() {
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/api/couriers").contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(3));
  }

  @Test
  @SneakyThrows
  @Sql(
      statements = {
        "INSERT INTO couriers (ID, FST_NME, LST_NME, ACTV) VALUES (1, 'Ben', 'Askew', 1)",
        "INSERT INTO couriers (ID, FST_NME, LST_NME, ACTV) VALUES (2, 'Anna', 'Green', 0)",
        "INSERT INTO couriers (ID, FST_NME, LST_NME, ACTV) VALUES (3, 'Harry', 'Scott', 1)"
      })
  @Sql(statements = "DELETE FROM couriers", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  public void testGetAllCouriers_shouldReturnOnlyActiveCouriers() {
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/api/couriers")
                .param("isActive", "true")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2));
  }

  @Test
  @SneakyThrows
  public void testGetAllCouriers_shouldReturnEmptyArray() {
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/api/couriers").contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(0));
  }
}
