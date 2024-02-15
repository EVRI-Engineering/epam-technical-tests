package com.evri.interview.service;

import com.evri.interview.model.Courier;
import com.evri.interview.repository.CourierEntity;
import com.evri.interview.repository.CourierRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourierServiceTest {
    @Mock
    CourierTransformer courierTransformer;
    @Mock
    CourierRepository repository;
    @InjectMocks
    CourierService service;

    @Test
    void getAllCouriers() {
        ArrayList<CourierEntity> courierEntities = new ArrayList<>();
        courierEntities.add(mock(CourierEntity.class));
        courierEntities.add(mock(CourierEntity.class));

        when(repository.findAll()).thenReturn(courierEntities);
        when(courierTransformer.toCourier(any())).thenReturn(mock(Courier.class));

        List<Courier> result = service.getAllCouriers(false);
        assertEquals(2, result.size());
    }

    @Test
    void getActiveCouriers() {
        CourierEntity activeCourierEntity = mock(CourierEntity.class);
        ArrayList<CourierEntity> courierEntities = new ArrayList<>();
        courierEntities.add(activeCourierEntity);
        courierEntities.add(mock(CourierEntity.class));
        Courier mockCourier = mock(Courier.class);
        Courier mockActiveCourier = mock(Courier.class);

        when(activeCourierEntity.isActive()).thenReturn(true);
        when(repository.findAll()).thenReturn(courierEntities);
        when(mockActiveCourier.isActive()).thenReturn(true);
        when(courierTransformer.toCourier(any())).thenReturn(mockActiveCourier).thenReturn(mockCourier);

        List<Courier> result = service.getAllCouriers(true);
        assertEquals(1, result.size());
        assertTrue(result.get(0).isActive());
    }

    @Test
    void findById() {
        when(repository.findById(1L)).thenReturn(Optional.of(mock(CourierEntity.class)));
        Optional<CourierEntity> result = service.findById(1L);

        assertTrue(result.isPresent());
    }

    @Test
    void update() {
        when(courierTransformer.toCourierEntity(any())).thenReturn(mock(CourierEntity.class));
        when(repository.save(any())).thenReturn(mock(CourierEntity.class));
        when(courierTransformer.toCourier(any())).thenReturn(mock(Courier.class));

        Courier result = service.update(Courier.builder().build());

        assertNotNull(result);
        verify(courierTransformer).toCourier(any());
        verify(courierTransformer).toCourierEntity(any());
        verify(repository).save(any());
    }

}