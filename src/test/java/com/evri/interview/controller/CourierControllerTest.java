package com.evri.interview.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

import com.evri.interview.model.Courier;
import com.evri.interview.model.UpdateCourier;
import com.evri.interview.service.CourierService;

class CourierControllerTest {

  private final CourierService service = mock(CourierService.class);
  private final CourierController controller = new CourierController(service);

  @Test
  void getAllCouriers_shouldReturnListOfCouriers() {
    // given
    List<Courier> couriers = Lists.list(new Courier(1, "John", true),
        new Courier(2, "Jane", false));
    when(service.getAllCouriers(false)).thenReturn(couriers);

    // when
    List<Courier> actual = controller.getAllCouriers(false);

    // then
    assertEquals(couriers, actual);
  }

  @Test
  void updateCourier_shouldReturnUpdatedCourier() {
    // given
    Courier courier = new Courier(1, "John Smith", true);
    when(service.updateCourier(1, new UpdateCourier("John", "Smith", true))).thenReturn(courier);

    // when
    Courier actual = controller.updateCourier(1, new UpdateCourier("John", "Smith", true));

    // then
    assertEquals(courier, actual);
  }
}