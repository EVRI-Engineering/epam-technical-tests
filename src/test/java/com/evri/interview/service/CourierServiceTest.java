package com.evri.interview.service;

import com.evri.interview.model.Courier;
import com.evri.interview.model.CourierDTO;
import com.evri.interview.repository.CourierEntity;
import com.evri.interview.repository.CourierRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CourierServiceTest {

  @Mock
  private CourierRepository courierRepository;

  @Mock
  private CourierTransformer courierTransformer;

  @InjectMocks
  private CourierService sut;

  @Test
  void shouldReturnAllCouriers() {
    CourierEntity firstCourierEntity = firstEntity();
    CourierEntity secondCourierEntity = secondEntity();
    Courier firstCourier = firstCourier();
    Courier secondCourier = secondCourier();

    when(courierRepository.findAll()).thenReturn(List.of(firstCourierEntity, secondCourierEntity));
    when(courierTransformer.toCourier(firstCourierEntity)).thenReturn(firstCourier);
    when(courierTransformer.toCourier(secondCourierEntity)).thenReturn(secondCourier);

    List<Courier> result = sut.getAllCouriers();

    assertThat(result)
        .hasSize(2)
        .containsOnly(firstCourier, secondCourier);
  }

  @Test
  void shouldReturnOnlyActiveCouriers() {
    CourierEntity firstCourierEntity = firstEntity();
    Courier firstCourier = secondCourier();

    when(courierRepository.findAllByActive(true)).thenReturn(List.of(firstCourierEntity));
    when(courierTransformer.toCourier(firstCourierEntity)).thenReturn(firstCourier);

    List<Courier> result = sut.getAllActiveCouriers();

    assertThat(result)
        .hasSize(1)
        .containsOnly(firstCourier);
  }

  @Test
  void shouldReturnUpdatedCourier() {
    CourierEntity firstCourierEntity = firstEntity();
    Courier firstCourier = firstCourier();

    when(courierRepository.findById(1L)).thenReturn(Optional.of(firstCourierEntity));
    when(courierRepository.save(firstCourierEntity)).thenReturn(firstCourierEntity);
    when(courierTransformer.toCourier(firstCourierEntity)).thenReturn(firstCourier);

    Courier result = sut.updateCourierById(1L, courierDTO());

    assertThat(result)
        .usingRecursiveComparison()
        .isEqualTo(firstCourier);
  }

  @Test
  void shouldThrowsExceptionWhenEntityIsNotFound() {
    CourierDTO courierDTO = courierDTO();

    when(courierRepository.findById(1L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> sut.updateCourierById(1L, courierDTO))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Courier with id 1 does not exist");
  }

  private CourierEntity firstEntity() {
    return CourierEntity.builder().id(1).firstName("Abby").lastName("AbbySecondName").active(true).build();
  }

  private Courier firstCourier() {
    return Courier.builder().id(1).name("Abby AbbySecondName").active(true).build();
  }

  private CourierEntity secondEntity() {
    return CourierEntity.builder().id(1).firstName("Ben").lastName("BenSecondName").active(false).build();
  }

  private Courier secondCourier() {
    return Courier.builder().id(1).name("Ben BenSecondName").active(false).build();
  }

  private CourierDTO courierDTO() {
    return new CourierDTO("Abby", "AbbySecondName", true);
  }

}
