package com.evri.interview.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.evri.interview.model.Courier;
import com.evri.interview.service.CourierService;

@ExtendWith(MockitoExtension.class)
class CourierControllerTest {

    @Mock
    private CourierService courierService;

    @InjectMocks
    private CourierController courierController;

    @Test
    void testGetAllCouriers() {
        List<Courier> mockCouriersList = Arrays.asList(
            Courier.builder().build(),
            Courier.builder().build()
        );

        when(courierService.getAllCouriers()).thenReturn(mockCouriersList);

        ResponseEntity<List<Courier>> responseEntity = courierController.getAllCouriers();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(mockCouriersList, responseEntity.getBody());

        verify(courierService, times(1)).getAllCouriers();
    }
}
