package com.evri.interview.unit;

import com.evri.interview.exception.CourierNotFoundException;
import com.evri.interview.model.Courier;
import com.evri.interview.repository.CourierEntity;
import com.evri.interview.repository.CourierRepository;
import com.evri.interview.service.CourierService;
import com.evri.interview.service.CourierTransformer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.evri.interview.CourierUtils.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CourierServiceTest {
    @Mock
    private CourierTransformer courierTransformer;
    @Mock
    private CourierRepository repository;

    @InjectMocks
    private CourierService courierService;

    private List<Courier> couriers;

    private List<CourierEntity> courierEntities;

    @BeforeEach
    void setup() {
        couriers = new ArrayList<>();
        couriers.add(courier());
        couriers.add(inactiveCourier());

        courierEntities = new ArrayList<>();
        courierEntities.add(courierEntity());
        courierEntities.add(inactiveCourierEntity());
    }

    @Test
    void shouldReturnAllCouriersWhenIsActiveNull() {
        when(repository.findAll()).thenReturn(courierEntities);
        when(courierTransformer.toCourier(courierEntities.get(0))).thenReturn(couriers.get(0));
        when(courierTransformer.toCourier(courierEntities.get(1))).thenReturn(couriers.get(1));

        List<Courier> actual = courierService.getCouriers(null);

        assertThat(actual.size()).isEqualTo(2);
        assertThat(actual.get(0)).isEqualTo(couriers.get(0));
        assertThat(actual.get(1)).isEqualTo(couriers.get(1));
        verify(repository).findAll();
        verifyNoMoreInteractions(repository);
        verify(courierTransformer, times(couriers.size())).toCourier(any());
        verifyNoMoreInteractions(courierTransformer);
    }

    @Test
    void shouldReturnActiveCouriersWhenIsActiveFlagTrue() {
        courierEntities.remove(1);
        when(repository.getAllByActive(true)).thenReturn(courierEntities);
        when(courierTransformer.toCourier(any())).thenReturn(couriers.get(0));

        List<Courier> actual = courierService.getCouriers(true);

        assertThat(actual.size()).isEqualTo(1);
        assertThat(actual.get(0)).isEqualTo(couriers.get(0));
        verify(repository).getAllByActive(true);
        verifyNoMoreInteractions(repository);
        verify(courierTransformer).toCourier(any());
        verifyNoMoreInteractions(courierTransformer);
    }

    @Test
    void shouldReturnInactiveCouriersWhenIsActiveFlagFalse() {
        courierEntities.remove(0);
        when(repository.getAllByActive(false)).thenReturn(courierEntities);
        when(courierTransformer.toCourier(any())).thenReturn(couriers.get(1));

        List<Courier> actual = courierService.getCouriers(false);

        assertThat(actual.size()).isEqualTo(1);
        assertThat(actual.get(0)).isEqualTo(couriers.get(1));
        verify(repository).getAllByActive(false);
        verifyNoMoreInteractions(repository);
        verify(courierTransformer).toCourier(any());
        verifyNoMoreInteractions(courierTransformer);
    }

    @Test
    void shouldUpdateCourier() {
        Courier courier = courier();
        Courier updated = Courier.builder()
                .id(courier.getId())
                .name("Updated Name")
                .active(courier.isActive())
                .build();
        CourierEntity courierEntity = courierEntity();
        CourierEntity updatedEntity = CourierEntity.builder()
                .id(courierEntity.getId())
                .firstName("Updated")
                .firstName("Name")
                .active(courierEntity.isActive())
                .build();
        when(repository.findById(courier.getId())).thenReturn(Optional.of(courierEntity));
        when(courierTransformer.toCourierEntity(any())).thenReturn(updatedEntity);
        when(repository.save(updatedEntity)).thenReturn(updatedEntity);
        when(courierTransformer.toCourier(updatedEntity)).thenReturn(updated);

        Courier actual = courierService.updateCourier(courier.getId(), updated);

        assertThat(actual.getId()).isEqualTo(updated.getId());
        assertThat(actual.getName()).isEqualTo(updated.getName());
        assertThat(actual.isActive()).isEqualTo(updated.isActive());
        verify(repository).findById(courier.getId());
        verify(courierTransformer).toCourierEntity(updated);
        verify(repository).save(updatedEntity);
        verify(courierTransformer).toCourier(updatedEntity);
        verifyNoMoreInteractions(repository);
        verifyNoMoreInteractions(courierTransformer);
    }

    @Test
    void shouldThrowExceptionWhenUpdateWithNonExistingId() {
        long invalidId = 999L;
        Courier courier = courier();
        courier.setId(invalidId);
        when(repository.findById(invalidId)).thenThrow(new CourierNotFoundException("Courier not found with id " + invalidId));

        assertThatThrownBy(() -> {
            courierService.updateCourier(courier.getId(), courier);
        }).isInstanceOf(CourierNotFoundException.class)
                .hasMessageContaining("Courier not found with id " + invalidId);
        verify(repository).findById(invalidId);
        verifyNoMoreInteractions(repository);
        verifyNoInteractions(courierTransformer);
    }
}
