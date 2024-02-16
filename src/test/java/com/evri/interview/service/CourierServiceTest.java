package com.evri.interview.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.evri.interview.exception.ResourceNotFoundException;
import com.evri.interview.model.Courier;
import com.evri.interview.repository.CourierEntity;
import com.evri.interview.repository.CourierRepository;

class CourierServiceTest {

  private final CourierRepository repository = mock(CourierRepository.class);
  private final CourierTransformer transformer = new CourierTransformer();

  private final CourierService service = new CourierService(transformer, repository);

  @Test
  void getAllCouriers_shouldReturnListOfActiveCouriers() {
    // given
    List<CourierEntity> couriersEntities = Lists.list(new CourierEntity(1, "John", "Smith", true));
    List<Courier> expected = Lists.list(new Courier(1, "John Smith", true));
    when(repository.findByActive(true)).thenReturn(couriersEntities);

    // when
    List<Courier> actual = service.getAllCouriers(true);

    // then
    assertEquals(expected, actual);
    verify(repository).findByActive(true);
  }

  @Test
  void getAllCouriers_shouldReturnListOfAllCouriers() {
    // given
    List<CourierEntity> couriersEntities = Lists.list(new CourierEntity(1, "John", "Smith", true));
    List<Courier> expected = Lists.list(new Courier(1, "John Smith", true));
    when(repository.findAll()).thenReturn(couriersEntities);

    // when
    List<Courier> actual = service.getAllCouriers(false);

    // then
    assertEquals(expected, actual);
    verify(repository).findAll();
  }

  @Test
  void getCourier_shouldReturnCourier() {
    // given
    CourierEntity courierEntity = new CourierEntity(1, "John", "Smith", true);
    when(repository.findById(1L)).thenReturn(java.util.Optional.of(courierEntity));

    // when
    CourierEntity actual = service.getCourier(1L);

    // then
    assertEquals(courierEntity, actual);
    verify(repository).findById(1L);
  }

  @Test
  void getCourier_shouldThrowResourceNotFoundException() {
    // given
    when(repository.findById(1L)).thenReturn(java.util.Optional.empty());

    // when
    ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> service.getCourier(1L));

    //then
    assertEquals("Courier not found, courierId: 1", exception.getMessage());
    verify(repository).findById(1L);
  }
}