package com.evri.interview.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@ActiveProfiles("test")
class CourierRepositoryTest {

  @Autowired private CourierRepository repository;

  @Test
  void findAll_shouldReturnEmptyList() {
    // when
    List<CourierEntity> courierEntities = repository.findAll();

    // then
    assertTrue(courierEntities.isEmpty());
  }

  @Test
  @Sql(
      statements = {
        "INSERT INTO couriers (ID, FST_NME, LST_NME, ACTV) VALUES (1, 'Ben', 'Askew', 1)",
        "INSERT INTO couriers (ID, FST_NME, LST_NME, ACTV) VALUES (2, 'Anna', 'Green', 0)",
        "INSERT INTO couriers (ID, FST_NME, LST_NME, ACTV) VALUES (3, 'Harry', 'Scott', 1)"
      })
  @Sql(statements = "DELETE FROM couriers", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  void findAll_shouldReturnAllEntities() {
    // when
    List<CourierEntity> courierEntities = repository.findAll();

    // then
    assertFalse(courierEntities.isEmpty());
    assertEquals(3, courierEntities.size());
  }

  @Test
  @Sql(
      statements = {
        "INSERT INTO couriers (ID, FST_NME, LST_NME, ACTV) VALUES (1, 'Ben', 'Askew', 1)",
        "INSERT INTO couriers (ID, FST_NME, LST_NME, ACTV) VALUES (2, 'Anna', 'Green', 0)",
        "INSERT INTO couriers (ID, FST_NME, LST_NME, ACTV) VALUES (3, 'Harry', 'Scott', 1)"
      })
  @Sql(statements = "DELETE FROM couriers", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  void findByActive_shouldReturnOnlyActiveCouriers() {
    // when
    List<CourierEntity> courierEntities = repository.findByActive(true);

    // then
    assertFalse(courierEntities.isEmpty());
    assertEquals(2, courierEntities.size());
    boolean isAllActive = courierEntities.stream().allMatch(CourierEntity::isActive);
    assertTrue(isAllActive);
  }
}
