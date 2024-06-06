package com.evri.interview.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.evri.interview.exception.CourierException;
import com.evri.interview.exception.ExceptionReason;
import com.evri.interview.exception.ExceptionTitle;
import com.evri.interview.model.Courier;
import com.evri.interview.repository.CourierEntity;
import com.evri.interview.repository.CourierRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class CourierServiceTest {

  private static final Long COURIER_ID = 1L;

  private final CourierRepository repository = mock(CourierRepository.class);
  private final CourierTransformer courierTransformer = mock(CourierTransformer.class);
  private final CourierService courierService = new CourierService(repository, courierTransformer);
  private final List<CourierEntity> couriers = new ArrayList<>();
  private final Courier courier = new Courier(COURIER_ID, "test test", true);

  @BeforeEach
  void setUp() {
    CourierEntity activeEntity = new CourierEntity(1, "test1", "test1", true);
    CourierEntity entity = new CourierEntity(2, "test2", "test2", false);

    couriers.add(activeEntity);
    couriers.add(entity);

    when(repository.findAll()).thenReturn(couriers);
    when(repository.findByActiveTrue()).thenReturn(Collections.singletonList(entity));
    when(repository.findById(COURIER_ID)).thenReturn(Optional.of(activeEntity));
    when(courierTransformer.toCourier(activeEntity)).thenReturn(courier);
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
  void getAllCouriers_ExceptionWasThrown() {
    when(repository.findByActiveTrue()).thenThrow(CourierException.class);

    CourierException actualException =
        Assertions.assertThrows(
            CourierException.class,
            () -> courierService.getAllCouriers(true));

    CourierException expectedException =
        new CourierException(
            ExceptionReason.INTERNAL_SERVICE_ERROR,
            HttpStatus.INTERNAL_SERVER_ERROR,
            ExceptionTitle.DATABASE_ERROR.getTitle());

    assertEquals(expectedException, actualException);
  }

  @Test
  void updateCourier_UpdateWithoutChanges() {
    Courier updatedCourier = courierService.updateCourier(COURIER_ID, courier);

    assertEquals(updatedCourier, courier);

    verify(repository).findById(COURIER_ID);
    verify(courierTransformer).toCourier(any());
  }

  @Test
  void updateCourier() {
    Courier courier = new Courier(COURIER_ID, "James Bond", true);

    Courier updatedCourier = courierService.updateCourier(COURIER_ID, courier);

    assertNotEquals(updatedCourier, courier);

    verify(repository).findById(COURIER_ID);
    verify(courierTransformer, times(2)).toCourier(any());
    verify(courierTransformer).update(any(), any());
    verify(repository).save(any());
  }

  @Test
  void updateCourier_ExceptionWasThrown() {
    when(repository.save(any())).thenThrow(CourierException.class);

    Courier courier = new Courier(COURIER_ID, "James Bond", true);

    CourierException actualException =
        Assertions.assertThrows(
            CourierException.class,
            () -> courierService.updateCourier(COURIER_ID, courier));

    CourierException expectedException =
        new CourierException(
            ExceptionReason.INTERNAL_SERVICE_ERROR,
            HttpStatus.INTERNAL_SERVER_ERROR,
            ExceptionTitle.DATABASE_ERROR.getTitle());

    assertEquals(expectedException, actualException);
  }
}