package com.evri.interview.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.evri.interview.model.Courier;
import com.evri.interview.repository.CourierEntity;
import com.evri.interview.repository.CourierRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CourierServiceTest {

  private final CourierRepository repository = mock(CourierRepository.class);
  private final CourierTransformer courierTransformer = mock(CourierTransformer.class);

  private final CourierService courierService = new CourierService(repository, courierTransformer);

  private final List<CourierEntity> couriers = new ArrayList<>();

  @BeforeEach
  void setUp() {
    CourierEntity activeEntity = new CourierEntity(1, "test1", "test1", true);
    CourierEntity entity = new CourierEntity(2, "test2", "test2", false);

    couriers.add(activeEntity);
    couriers.add(entity);

    when(repository.findAll()).thenReturn(couriers);
    when(repository.findByActiveTrue()).thenReturn(Collections.singletonList(entity));

  }

  @Test
  void getAllCouriers_IsActiveFalse() {
    List<Courier> couriers = courierService.getAllCouriers(false);

    assertEquals(2, couriers.size());

    verify(repository).findAll();
    verify(courierTransformer, times(2)).toCourier(any());
  }

  @Test
  void getAllCouriers_IsActiveTrue() {
    List<Courier> couriers = courierService.getAllCouriers(true);

    assertEquals(1, couriers.size());

    verify(repository).findByActiveTrue();
    verify(courierTransformer).toCourier(any());
  }

  @Test
  void getAllCouriers_NoCouriers() {
    when(repository.findByActiveTrue()).thenReturn(Collections.emptyList());

    List<Courier> couriers = courierService.getAllCouriers(true);

    assertTrue(couriers.isEmpty());
  }

  @Test
  void updateCourier() {
  }
}