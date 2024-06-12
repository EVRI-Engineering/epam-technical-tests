package com.evri.interview.service;

import com.evri.interview.controller.dto.CourierDto;
import com.evri.interview.controller.exception.CourierNotFoundException;
import com.evri.interview.model.Courier;
import com.evri.interview.repository.CourierEntity;
import com.evri.interview.repository.CourierRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class CourierServiceTest {

    private static final long ID_1 = 1L;
    private static final long ID_2 = 2L;

    @Mock
    private CourierTransformer courierTransformer;
    @Mock
    private CourierRepository repository;

    @InjectMocks
    private CourierService courierService;

    @Test
    void givenIsActive_whenGetAllCouriers_thenReturnOnlyActiveCouriers() {
        // given
        Courier courier = Courier.builder().id(ID_1).active(true).build();
        CourierEntity courierEntity = CourierEntity.builder().id(ID_1).active(true).build();
        List<Courier> expected = new ArrayList<>();
        expected.add(courier);

        List<CourierEntity> activeCouriers = new ArrayList<>();
        activeCouriers.add(courierEntity);

        given(repository.findAllByActiveTrue()).willReturn(activeCouriers);
        given(courierTransformer.toCourier(courierEntity)).willReturn(courier);

        // when
        List<Courier> actual = courierService.getAllCouriers(true);

        // then
        Assertions.assertThat(actual).isEqualTo(expected);
        verify(repository).findAllByActiveTrue();
        verify(courierTransformer).toCourier(courierEntity);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void givenIsNotActive_whenGetAllCouriers_thenReturnAllCouriers() {
        // given
        Courier courier1 = Courier.builder().id(ID_1).active(true).build();
        Courier courier2 = Courier.builder().id(ID_2).active(false).build();
        CourierEntity courierEntity1 = CourierEntity.builder().id(ID_1).active(true).build();
        CourierEntity courierEntity2 = CourierEntity.builder().id(ID_2).active(false).build();

        List<Courier> expected = new ArrayList<>();
        expected.add(courier1);
        expected.add(courier2);

        List<CourierEntity> courierEntities = new ArrayList<>();
        courierEntities.add(courierEntity1);
        courierEntities.add(courierEntity2);

        given(repository.findAll()).willReturn(courierEntities);
        given(courierTransformer.toCourier(courierEntity1)).willReturn(courier1);
        given(courierTransformer.toCourier(courierEntity2)).willReturn(courier2);

        // when
        List<Courier> actual = courierService.getAllCouriers(false);

        // then
        Assertions.assertThat(actual).isEqualTo(expected);
        verify(repository).findAll();
        verify(courierTransformer).toCourier(courierEntity1);
        verify(courierTransformer).toCourier(courierEntity2);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void givenValidId_whenUpdateCourier_thenReturnUpdatedCourier() {
        // given
        String newFirstName = "Ben";
        String newLastName = "Askew";
        String newName = newFirstName + " " + newLastName;

        Courier expected = Courier.builder().id(ID_1).name(newName).active(true).build();
        CourierDto dto = CourierDto.builder().name(newName).active(true).build();
        CourierEntity existingEntity = CourierEntity.builder().firstName("Den").lastName("Caskew").id(ID_1).active(true).build();
        CourierEntity newEntity = CourierEntity.builder().firstName(newFirstName).lastName(newLastName).id(ID_1).active(true).build();

        given(repository.findById(ID_1)).willReturn(Optional.of(existingEntity));
        given(repository.save(any(CourierEntity.class))).willReturn(newEntity);
        given(courierTransformer.toCourier(newEntity)).willReturn(expected);

        // when
        Courier actual = courierService.updateCourier(ID_1, dto);

        // then
        Assertions.assertThat(actual).isEqualTo(expected);
        verify(repository).findById(ID_1);
        verify(courierTransformer).merge(existingEntity, dto);
        verify(repository).save(existingEntity);
        verify(courierTransformer).toCourier(newEntity);

        verifyNoMoreInteractions(repository);
    }

    @Test
    void givenInvalidId_whenUpdateCourier_thenThrowNotFoundException() {
        // given
        String exception_message = "Courier with id 10 was not found";

        CourierDto dto = CourierDto.builder().name("Ben Askew").active(true).build();

        // when-then
        assertThatThrownBy(() -> courierService.updateCourier(10L, dto))
                .isInstanceOf(CourierNotFoundException.class)
                .hasMessage(exception_message);
    }
}