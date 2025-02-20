package com.evri.interview.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.evri.interview.model.Courier;
import com.evri.interview.repository.CourierEntity;
import com.evri.interview.repository.CourierRepository;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CourierServiceTest {

  @Mock
  private CourierRepository repository;

  @Mock
  private CourierTransformer courierTransformer;

  @InjectMocks
  private CourierService courierService;

  @Test
  public void getAllCouriers_ReturnsOnlyActiveCouriers() {
    List<CourierEntity> entities = Arrays.asList(
        CourierEntity.builder().id(1L).firstName("Ben").lastName("Askew").active(true).build(),
        CourierEntity.builder().id(2L).firstName("John").lastName("Doe").active(false).build());
    when(repository.findAll()).thenReturn(entities);
    when(courierTransformer.toCourier(any())).thenAnswer(invocationOnMock -> {
      CourierEntity entity = invocationOnMock.getArgument(0);
      return Courier.builder()
          .id(entity.getId())
          .firstName(entity.getFirstName())
          .lastName(entity.getLastName())
          .active(entity.isActive())
          .build();
    });

    List<Courier> couriers = courierService.getAllCouriers(true);

    assertEquals(1, couriers.size());
    assertEquals(1L, couriers.get(0).getId());
    assertEquals("Ben", couriers.get(0).getFirstName());
    assertEquals("Askew", couriers.get(0).getLastName());
  }

  @Test
  public void getAllCouriers_ReturnsAllCouriers() {
    List<CourierEntity> entities = Arrays.asList(
        CourierEntity.builder().id(1L).firstName("Ben").lastName("Askew").active(true).build(),
        CourierEntity.builder().id(2L).firstName("John").lastName("Doe").active(false).build());
    when(repository.findAll()).thenReturn(entities);
    when(courierTransformer.toCourier(any())).thenAnswer(invocationOnMock -> {
      CourierEntity entity = invocationOnMock.getArgument(0);
      return Courier.builder()
          .id(entity.getId())
          .firstName(entity.getFirstName())
          .lastName(entity.getLastName())
          .active(entity.isActive())
          .build();
    });

    List<Courier> couriers = courierService.getAllCouriers(false);

    assertEquals(2, couriers.size());
    assertEquals(1L, couriers.get(0).getId());
    assertEquals("Ben", couriers.get(0).getFirstName());
    assertEquals("Askew", couriers.get(0).getLastName());
    assertEquals(2L, couriers.get(1).getId());
    assertEquals("John", couriers.get(1).getFirstName());
    assertEquals("Doe", couriers.get(1).getLastName());
  }

  @Test
  public void getUpdateCourier_ReturnsNotFound() {
    Courier updatedCourier = Courier.builder()
        .id(99L)
        .firstName("Andrew")
        .lastName("Jackson")
        .active(true)
        .build();
    when(repository.findById(anyLong())).thenReturn(Optional.empty());

    Optional<Courier> result = courierService.updateCourier(99L, updatedCourier);
    assertFalse(result.isPresent());
  }

  @Test
  public void getUpdateCourier_ReturnsSuccess() {
    CourierEntity existingEntity = CourierEntity.builder()
        .id(2L)
        .firstName("John")
        .lastName("Doe")
        .active(false)
        .build();
    CourierEntity savedEntity = CourierEntity.builder()
        .id(2L)
        .firstName("Andrew")
        .lastName("Jackson")
        .active(true)
        .build();
    Courier updatedCourier = Courier.builder()
        .id(2L)
        .firstName("Andrew")
        .lastName("Jackson")
        .active(true)
        .build();

    when(repository.findById(2L)).thenReturn(Optional.of(existingEntity));
    when(repository.save(any(CourierEntity.class))).thenReturn(savedEntity);
    when(courierTransformer.toCourier(any(CourierEntity.class))).thenReturn(updatedCourier);

    Optional<Courier> result = courierService.updateCourier(2L, updatedCourier);
    assertTrue(result.isPresent());
    assertEquals("Andrew", result.get().getFirstName());
    assertEquals("Jackson", result.get().getLastName());
    assertTrue(result.get().isActive());

    verify(repository, times(1)).findById(2L);
    verify(repository, times(1)).save(existingEntity);
    verify(courierTransformer, times(1)).toCourier(savedEntity);
  }
}
