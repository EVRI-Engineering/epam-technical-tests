package com.evri.interview.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.evri.interview.model.Courier;
import com.evri.interview.repository.CourierEntity;
import com.evri.interview.repository.CourierRepository;

@ExtendWith(MockitoExtension.class)
class CourierServiceTest {

    @Mock
    private CourierRepository courierRepository;
    @Mock
    private CourierTransformer courierTransformer;
    @InjectMocks
    private CourierService courierService;

    @Test
    void shouldCallFindAllCouriers_WhenFalseActiveFlag() {
        //given
        //when
        when(courierRepository.findAll()).thenReturn(Arrays.asList(mock(CourierEntity.class)));
        when(courierTransformer.toCourier(any())).thenReturn(mock(Courier.class));

        final List<Courier> couriers = courierService.getAllCouriers(false);
        verify(courierRepository, never()).findAllByActiveTrue();

        assertThat(couriers).hasSize(1);
    }

    @Test
    void shouldCallFindActiveCouriers_WhenTrueActiveFlag() {

        when(courierRepository.findAllByActiveTrue()).thenReturn(Arrays.asList(mock(CourierEntity.class)));
        when(courierTransformer.toCourier(any())).thenReturn(mock(Courier.class));

        final List<Courier> couriers = courierService.getAllCouriers(true);
        verify(courierRepository, never()).findAll();

        assertThat(couriers).hasSize(1);
    }
}