package com.evri.interview.controller;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import com.evri.interview.model.Courier;
import com.evri.interview.service.CourierService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import java.util.Arrays;
import java.util.List;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class CourierControllerTest {

    @Mock
    private CourierService courierService;

    @InjectMocks
    private CourierController courierController;

    @Test
    void testGetAllCouriers() {
        List<Courier> mockCouriers = Arrays.asList(Courier.builder().id(1L).name("John Doe").active(true).build());
        when(courierService.getCouriers(false)).thenReturn(mockCouriers);
        ResponseEntity<List<Courier>> response = courierController.getAllCouriers(false);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testUpdateCourier() {
        Courier courier = Courier.builder().id(1L).name("John Doe").active(true).build();
        when(courierService.updateCourier(1L, courier)).thenReturn(courier);
        ResponseEntity<Courier> response = courierController.updateCourier(1L, courier);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("John Doe", response.getBody().getName());
    }
}