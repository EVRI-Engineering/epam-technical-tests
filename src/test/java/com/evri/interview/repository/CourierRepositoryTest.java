package com.evri.interview.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DataJpaTest
class CourierRepositoryTest {

  public static final long SEARCHED_ID = 123L;
  public static final long ACTIVE_ID = 12L;
  @Autowired
  CourierRepository tested;

  @BeforeAll
  void init() {
    tested.deleteAll();
    CourierEntity courier1 = buildEntity(10L, "fN1", "lN1", false);
    CourierEntity courier2 = buildEntity(SEARCHED_ID, "fN2", "lN2", false);
    CourierEntity courier3 = buildEntity(ACTIVE_ID, "fN3", "lN3", true);
    CourierEntity courier4 = buildEntity(150L, "fN4", "lN4", false);
    tested.saveAll(List.of(courier1, courier2, courier3, courier4));
  }

  @AfterAll
  void tearDown() {
    tested.deleteAll();
  }

  @Test
  void shouldFindById() {
    CourierEntity result = tested.findById(SEARCHED_ID).orElseThrow();
    assertThat(result.getId()).isEqualTo(SEARCHED_ID);
  }

  @Test
  void shouldFindAllActive() {
    List<CourierEntity> result = tested.findAllByActiveTrue();
    assertThat(result).hasSize(1);
    assertThat(result.get(0).getId()).isEqualTo(ACTIVE_ID);

  }

  CourierEntity buildEntity(Long id, String firstName, String lastName, boolean isActive) {
    return CourierEntity.builder()
      .id(id)
      .firstName(firstName)
      .lastName(lastName)
      .active(isActive)
      .build();

  }
}