package com.evri.interview.service;

import com.evri.interview.exception.CourierNotFoundException;
import com.evri.interview.model.Courier;
import com.evri.interview.entity.CourierEntity;
import com.evri.interview.repository.CourierRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
        //given
        CourierEntity entity = CourierEntity.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .active(true)
                .build();
        when(repository.findAll())
                .thenReturn(Collections.singletonList(entity));

        Courier expectedCourier = Courier.builder()
                .id(1L)
                .name("John Doe")
                .active(true)
                .build();
        when(courierTransformer.toCourier(entity))
                .thenReturn(expectedCourier);

        //when
        List<Courier> couriers = courierService.getCouriers(false);

        //then
        assertEquals(1, couriers.size());
        Courier actualCourier = couriers.get(0);
        assertEquals(expectedCourier.getId(), actualCourier.getId());
        assertEquals(expectedCourier.getName(), actualCourier.getName());
        assertEquals(expectedCourier.getActive(), actualCourier.getActive());
        
        verify(repository, times(1)).findAll();
        verify(courierTransformer, times(1)).toCourier(entity);
    }

    @Test
    void testGetAllActiveCouriers() {
        //given
        CourierEntity entity = CourierEntity.builder()
                .id(1L)
                .firstName("Alice")
                .lastName("Smith")
                .active(true)
                .build();
        when(repository.findByActiveTrue())
                .thenReturn(Collections.singletonList(entity));

        Courier expectedCourier = Courier.builder()
                .id(1L)
                .name("Alice Smith")
                .active(true)
                .build();
        when(courierTransformer.toCourier(entity))
                .thenReturn(expectedCourier);

        //when
        List<Courier> activeCouriers = courierService.getCouriers(true);

        //then
        assertEquals(1, activeCouriers.size());
        Courier actualCourier = activeCouriers.get(0);
        assertEquals(expectedCourier.getId(), actualCourier.getId());
        assertEquals(expectedCourier.getName(), actualCourier.getName());
        assertEquals(expectedCourier.getActive(), actualCourier.getActive());

        verify(repository, times(1)).findByActiveTrue();
        verify(courierTransformer, times(1)).toCourier(entity);
    }

    @Test
    void testUpdateCourierDetails_CourierFound() {
        //given
        Long courierId = 1L;
        Courier newCourier = Courier.builder()
                .name("Jane Doe")
                .active(false)
                .build();
        CourierEntity existingEntity = CourierEntity.builder()
                .id(courierId)
                .firstName("John")
                .lastName("Doe")
                .active(true)
                .build();
        CourierEntity updatedEntity = CourierEntity.builder()
                .id(courierId)
                .firstName("John")
                .lastName("Doe")
                .active(false)
                .build();

        when(repository.findById(courierId))
                .thenReturn(Optional.of(existingEntity));
        when(courierTransformer.toUpdatedCourierEntity(existingEntity, newCourier))
                .thenReturn(updatedEntity);

        //when
        assertDoesNotThrow(() -> courierService.updateCourierDetails(newCourier, courierId));

        //then
        verify(repository, times(1)).findById(courierId);
        verify(courierTransformer, times(1)).toUpdatedCourierEntity(existingEntity, newCourier);
        verify(repository, times(1)).save(any(CourierEntity.class));
    }

    @Test
    void testUpdateCourierDetails_CourierNotFound() {
        //given
        Long courierId = 1L;
        Courier newCourier = Courier.builder()
                .name("Jane Doe")
                .active(false)
                .build();

        when(repository.findById(courierId)).thenReturn(Optional.empty());

        //when
        CourierNotFoundException exception = assertThrows(CourierNotFoundException.class,
                () -> courierService.updateCourierDetails(newCourier, courierId));

        //then
        assertEquals("Courier with id 1 was not found", exception.getMessage());

        verify(repository, times(1)).findById(courierId);
        verify(courierTransformer, never()).toUpdatedCourierEntity(any(CourierEntity.class), any(Courier.class));
        verify(repository, never()).save(any(CourierEntity.class));
    }
}
