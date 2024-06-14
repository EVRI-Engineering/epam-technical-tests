package com.evri.interview.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.evri.interview.model.Courier;
import com.evri.interview.repository.CourierRepository;

@ExtendWith(MockitoExtension.class)
public class CourierServiceTest {

    @Mock
    private CourierRepository courierRepository;

    @Mock
    private CourierTransformer courierTransformer;

    @InjectMocks
    private CourierService courierService;

    private Courier courier;

    @BeforeEach
    void setUp() {
        courier = Courier.builder().id(1L).firstName("Tom").lastName("Gates").active(true).build();
    }

    @Test
    void testGetCouriersOnlyActive() {
        when(courierRepository.findByActiveTrue()).thenReturn(Collections.singletonList(courier));

        List<Courier> couriers = courierService.getCouriers(true);

        assertEquals(1, couriers.size());
        assertEquals(courier, couriers.get(0));
        verify(courierRepository, times(1)).findByActiveTrue();
    }

    @Test
    void testGetCouriersAll() {
        when(courierRepository.findAll()).thenReturn(Collections.singletonList(courier));
        when(courierTransformer.toCourier(courier)).thenReturn(courier);

        List<Courier> couriers = courierService.getCouriers(false);

        assertEquals(1, couriers.size());
        assertEquals(courier, couriers.get(0));
        verify(courierRepository, times(1)).findAll();
    }

    @Test
    void testUpdateCourier() {
        Long courierId = 1L;
        String firstName = "UpdatedFirstName";
        String lastName = "UpdatedLastName";
        boolean active = true;

        Courier updatedCourier = Courier.builder()
                .id(courierId)
                .firstName(firstName)
                .lastName(lastName)
                .active(active)
                .build();

        when(courierRepository.findById(courierId)).thenReturn(Optional.of(courier));
        when(courierRepository.save(any(Courier.class))).thenReturn(updatedCourier);
        when(courierTransformer.toCourier(updatedCourier)).thenReturn(updatedCourier);

        Optional<Courier> result = courierService.updateCourier(courierId, firstName, lastName, active);

        assertTrue(result.isPresent());
        assertEquals(updatedCourier, result.get());
        verify(courierRepository, times(1)).findById(courierId);
        verify(courierRepository, times(1)).save(any(Courier.class));
    }

    @Test
    void testUpdateCourierNotFound() {
        Long courierId = 3L;

        when(courierRepository.findById(courierId)).thenReturn(Optional.empty());

        Optional<Courier> result = courierService.updateCourier(courierId, "FirstName", "LastName", true);

        assertFalse(result.isPresent());
        verify(courierRepository, times(1)).findById(courierId);
        verify(courierRepository, times(0)).save(any(Courier.class));
    }
}
