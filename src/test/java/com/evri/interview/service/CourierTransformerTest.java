package com.evri.interview.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.evri.interview.model.Courier;
import com.evri.interview.model.UpdateCourier;
import com.evri.interview.repository.CourierEntity;

class CourierTransformerTest {

  private static final CourierTransformer transformer = new CourierTransformer();

  @Test
  void toCourier_shouldTransformCourierEntityToCourier() {
    // given
    CourierEntity courierEntity = new CourierEntity(1, "John", "Smith", true);

    // when
    Courier courier = transformer.toCourier(courierEntity);

    // then
    assertEquals(1, courier.getId());
    assertEquals("John Smith", courier.getName());
    assertTrue(courier.isActive());
  }

  @Test
  void updateCourierEntity() {
    // given
    CourierEntity courierEntity = new CourierEntity(1, "John", "Smith", true);

    // when
    transformer.updateCourierEntity(courierEntity, new UpdateCourier("Jane", "Doe", false));

    // then
    assertEquals("Jane", courierEntity.getFirstName());
    assertEquals("Doe", courierEntity.getLastName());
    assertFalse(courierEntity.isActive());
  }
}