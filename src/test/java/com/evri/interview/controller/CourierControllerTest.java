package com.evri.interview.controller;

import com.evri.interview.model.Courier;
import com.evri.interview.service.CourierService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CourierControllerTest {

    @Mock
    private CourierService courierService;

    @InjectMocks
    private CourierController courierController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllCouriers_whenIsActiveIsFalse_returnsAllCouriers() {
        List<Courier> couriers = Arrays.asList(
                new Courier(1L, "Jack", "Disney", true),
                new Courier(2L, "Rick", "Salt", false)
        );
        when(courierService.getCouriers(false)).thenReturn(couriers);

        ResponseEntity<List<Courier>> response = courierController.getAllCouriers(false);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(couriers, response.getBody());
    }

    @Test
    void getAllCouriers_whenIsActiveIsTrue_returnsActiveCouriers() {
        List<Courier> couriers = List.of(
                new Courier(1L, "Jack", "Disney", true)
        );
        when(courierService.getCouriers(true)).thenReturn(couriers);

        ResponseEntity<List<Courier>> response = courierController.getAllCouriers(true);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(couriers, response.getBody());
    }

    @Test
    void testUpdateCourier_whenCourierExists_updatesAndReturnsCourier() {
        Long courierId = 1L;
        Courier courier = new Courier(courierId, "Jack", "Disney", true);
        when(courierService.updateCourier(courierId, "Jack", "Disney", true)).thenReturn(Optional.of(courier));

        ResponseEntity<Courier> response = courierController.updateCourier(courierId, courier);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(courier, response.getBody());
    }

    @Test
    void testUpdateCourier_whenCourierDoesNotExist_throwsNotFoundException() {
        Long courierId = 1L;
        Courier courier = new Courier(courierId, "Jack", "Disney", true);
        when(courierService.updateCourier(courierId, "Jack", "Disney", true)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            courierController.updateCourier(courierId, courier);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }
}
