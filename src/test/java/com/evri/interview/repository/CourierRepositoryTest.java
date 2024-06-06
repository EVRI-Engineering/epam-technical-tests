package com.evri.interview.repository;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class CourierRepositoryTest {

  @Autowired
  private CourierRepository courierRepository;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void getAllActiveCouriers() {
    List<CourierEntity> activeCouriers = courierRepository.findByActiveTrue();

    assertFalse(activeCouriers.isEmpty());
    assertTrue(activeCouriers.stream().allMatch(CourierEntity::isActive));
  }
}