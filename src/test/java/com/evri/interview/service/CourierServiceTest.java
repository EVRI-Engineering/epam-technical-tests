package com.evri.interview.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.evri.interview.exception.ResourceNotFoundException;
import com.evri.interview.model.Courier;
import com.evri.interview.repository.CourierEntity;
import com.evri.interview.repository.CourierRepository;
import com.evri.interview.request.CourierRequestBody;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class CourierServiceTest {

    @Mock
    private CourierTransformer courierTransformer;

    @Mock
    private CourierRepository repository;

    @InjectMocks
    private CourierService courierService;

    @Test
    void testGetAllCouriers() {
        List<CourierEntity> mockCourierEntitiesList = Arrays.asList(
                CourierEntity.builder().active(false).build(),
                CourierEntity.builder().active(true).build()
        );

        when(repository.findAll()).thenReturn(mockCourierEntitiesList);

        List<Courier> couriersList = courierService.getAllCouriers(false);

        assertEquals(couriersList.size(), mockCourierEntitiesList.size());

        verify(repository, times(1)).findAll();
    }

    @Test
    void testGetAllActiveCouriers() {
        List<CourierEntity> mockCourierEntitiesList = Arrays.asList(
                CourierEntity.builder().active(true).build()
        );

        when(repository.findAllByActiveTrue()).thenReturn(mockCourierEntitiesList);

        List<Courier> couriersList = courierService.getAllCouriers(true);

        assertEquals(1, couriersList.size());

        verify(repository, times(1)).findAllByActiveTrue();
    }

    @Test
    void testUpdateCourierById_Success() {
        long courierIdForUpdate = 1L;

        CourierEntity mockCourierEntity = CourierEntity.builder()
                .id(courierIdForUpdate)
                .build();

        Courier mockCourier = Courier.builder()
                .id(courierIdForUpdate)
                .name("Sam Stone")
                .build();

        CourierRequestBody mockCourierRequestBody = CourierRequestBody.builder()
                .firstName("Sam")
                .lastName("Stone")
                .build();
        
        when(courierTransformer.toCourier(mockCourierEntity)).thenReturn(mockCourier);
        when(repository.findById(courierIdForUpdate)).thenReturn(Optional.ofNullable(mockCourierEntity));
        when(repository.save(Objects.requireNonNull(mockCourierEntity))).thenReturn(mockCourierEntity);

        Courier courier = courierService.updateCourierById(courierIdForUpdate, mockCourierRequestBody);

        assertEquals("Sam Stone", courier.getName());

        verify(courierTransformer, times(1)).toCourier(mockCourierEntity);
        verify(repository, times(1)).findById(courierIdForUpdate);
        verify(repository, times(1)).save(mockCourierEntity);
    }

    @Test
    void testUpdateCourierById_CourierNotFound() {
        long notExistCourierId = 1000L;

        CourierRequestBody mockCourierRequestBody = CourierRequestBody.builder()
                .firstName("Sam")
                .lastName("Stone")
                .build();
        
        when(repository.findById(notExistCourierId)).thenThrow(
                new ResourceNotFoundException(
                        String.format("Not found courier with courierId: %s", notExistCourierId)
                )
        );


        ResourceNotFoundException resourceNotFoundException = assertThrows(
                ResourceNotFoundException.class,
                () -> courierService.updateCourierById(notExistCourierId, mockCourierRequestBody)
        );

        assertEquals(
                String.format("Not found courier with courierId: %s", notExistCourierId),
                resourceNotFoundException.getMessage()
        );

        verify(repository, times(1)).findById(notExistCourierId);
    }
}
