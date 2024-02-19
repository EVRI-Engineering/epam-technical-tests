package com.evri.interview.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

import com.evri.interview.exception.ResourceNotFoundException;
import com.evri.interview.model.Courier;
import com.evri.interview.request.CourierRequestBody;
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

        when(courierService.getAllCouriers(false)).thenReturn(mockCouriersList);

        ResponseEntity<List<Courier>> responseEntity = courierController.getAllCouriers(false);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(mockCouriersList, responseEntity.getBody());

        verify(courierService, times(1)).getAllCouriers(false);
    }

    @Test
    void testUpdateCourierById_Success() {
        long courierIdForUpdate = 1L;
        
        CourierRequestBody mockCourierRequestBody = CourierRequestBody.builder().build();
        Courier mockCourier = Courier.builder().build();

        when(courierService.updateCourierById(courierIdForUpdate, mockCourierRequestBody)).thenReturn(mockCourier);

        ResponseEntity<Courier> responseEntity = courierController.updateCourierById(
            courierIdForUpdate, mockCourierRequestBody
        );

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(mockCourier, responseEntity.getBody());

        verify(courierService, times(1))
            .updateCourierById(courierIdForUpdate, mockCourierRequestBody);
    }

    @Test
    void testUpdateCourierById_CourierNotFound() { 
        long notExistCourierId = 1000L;
        
        CourierRequestBody mockCourierRequestBody = CourierRequestBody.builder().build();

        when(courierService.updateCourierById(notExistCourierId, mockCourierRequestBody))
            .thenThrow(
                new ResourceNotFoundException(
                    String.format("Not found courier with courierId: %s", notExistCourierId)
                )
            )
        ;
        
        ResourceNotFoundException resourceNotFoundException = assertThrows(
            ResourceNotFoundException.class,
            () -> courierController.updateCourierById(notExistCourierId, mockCourierRequestBody)
        );
            
        assertEquals(
            String.format("Not found courier with courierId: %s", notExistCourierId),
            resourceNotFoundException.getMessage()
        );
    }
}
