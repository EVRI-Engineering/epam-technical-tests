package com.evri.interview.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.evri.interview.dto.CourierDto;
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

    private CourierEntity courierEntity1;
    private CourierEntity courierEntity2;
    private Courier courier1;
    private Courier courier2;

    @BeforeEach
    void setUp() {
        courierEntity1 = new CourierEntity(1L, "John", "Doe", true);
        courierEntity2 = new CourierEntity(2L, "Jane", "Doe", false);

        courier1 = Courier.builder()
                .id(1L)
                .name("John Doe")
                .active(true).build();
        courier2 = Courier.builder()
                .id(2L)
                .name("Jane Doe")
                .active(false).build();
    }

    @Test
    void getAllCouriers_active() {
        when(courierRepository.findByActive(true)).thenReturn(Collections.singletonList(courierEntity1));
        when(courierTransformer.toCourier(courierEntity1)).thenReturn(courier1);

        List<Courier> couriers = courierService.getAllCouriers(true);

        assertEquals(1, couriers.size());
        assertEquals(courier1, couriers.get(0));
    }

    @Test
    void getAllCouriers() {
        when(courierRepository.findAll()).thenReturn(Arrays.asList(courierEntity1, courierEntity2));
        when(courierTransformer.toCourier(courierEntity1)).thenReturn(courier1);
        when(courierTransformer.toCourier(courierEntity2)).thenReturn(courier2);

        List<Courier> couriers = courierService.getAllCouriers(false);

        assertEquals(2, couriers.size());
        assertEquals(courier1, couriers.get(0));
        assertEquals(courier2, couriers.get(1));
    }

    @Test
    void updateCourier_success() {
        CourierDto courierDto = new CourierDto("John", "Smith", true);
        when(courierRepository.findById(1L)).thenReturn(Optional.of(courierEntity1));
        when(courierRepository.save(any(CourierEntity.class))).thenReturn(courierEntity1);
        when(courierTransformer.toCourier(any(CourierEntity.class))).thenReturn(courier1);

        Optional<Courier> updatedCourier = courierService.updateCourier(1L, courierDto);

        assertTrue(updatedCourier.isPresent());
        assertEquals(courier1, updatedCourier.get());
        verify(courierRepository).save(any(CourierEntity.class));
    }

    @Test
    void updateCourier_doesNOT_exist() {
        CourierDto courierDto = new CourierDto("John", "Smith", true);
        when(courierRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Courier> updatedCourier = courierService.updateCourier(1L, courierDto);

        assertFalse(updatedCourier.isPresent());
        verify(courierRepository, never()).save(any(CourierEntity.class));
    }
}