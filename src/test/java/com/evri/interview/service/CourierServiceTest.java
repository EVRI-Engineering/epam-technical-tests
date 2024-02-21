package com.evri.interview.service;

import com.evri.interview.exception.EntityNotFoundException;
import com.evri.interview.model.Courier;
import com.evri.interview.repository.CourierEntity;
import com.evri.interview.repository.CourierRepository;
import com.evri.interview.service.impl.CourierServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CourierServiceTest {

    @Mock
    private CourierRepository repository;

    @Mock
    private CourierTransformer transformer;

    @InjectMocks
    private CourierServiceImpl service;

    @Test
    public void shouldGetAllCouriers() {
        CourierEntity courierEntity1 = buildCourierEntity(1L, "John", "Doe", true);
        CourierEntity courierEntity2 = buildCourierEntity(2L, "John", "Doe", false);
        List<CourierEntity> entities = Arrays.asList(courierEntity1, courierEntity2);

        Courier courier1 = buildCourier(1L, "John Doe", true);
        Courier courier2 = buildCourier(2L, "John Doe", false);

        List<Courier> couriers = Arrays.asList(courier1, courier2);

        when(repository.findAll()).thenReturn(entities);
        when(transformer.toCourier(courierEntity1)).thenReturn(courier1);
        when(transformer.toCourier(courierEntity2)).thenReturn(courier2);

        List<Courier> result = service.getAllCouriers();

        verify(repository).findAll();
        verify(transformer, times(entities.size())).toCourier(any(CourierEntity.class));
        assertEquals(couriers.size(), result.size());
        assertTrue(result.containsAll(couriers));
    }

    @Test
    public void shouldGetActiveCouriers() {
        CourierEntity courierEntity1 = buildCourierEntity(1L, "John", "Doe", true);
        List<CourierEntity> entities = Collections.singletonList(courierEntity1);

        Courier courier1 = buildCourier(1L, "John Doe", true);
        List<Courier> couriers = Collections.singletonList(courier1);

        when(repository.findAllByActive(true)).thenReturn(entities);
        when(transformer.toCourier(courierEntity1)).thenReturn(courier1);

        List<Courier> result = service.getActiveCouriers();

        verify(repository).findAllByActive(true);
        verify(transformer, times(entities.size())).toCourier(any(CourierEntity.class));
        assertEquals(couriers.size(), result.size());
        assertTrue(result.containsAll(couriers));
    }

    @Test
    public void shouldUpdateCourier() {
        long courierId = 1L;
        Courier courierDto = buildCourier(courierId, "John Doe", true);
        CourierEntity courierEntity = buildCourierEntity(courierId, "John", "Doe Jun.", true);
        Courier updatedCourier = buildCourier(courierId, "John Doe Jun.", true);

        when(repository.findById(courierId)).thenReturn(Optional.of(courierEntity));
        when(transformer.toCourierEntity(courierDto)).thenReturn(courierEntity);
        when(transformer.toCourier(courierEntity)).thenReturn(updatedCourier);

        Courier result = service.updateCourierById(courierDto, courierId);

        verify(repository).findById(courierId);
        verify(transformer).toCourierEntity(courierDto);
        verify(repository).save(courierEntity);
        verify(transformer).toCourier(courierEntity);

        assertEquals(updatedCourier, result);
    }

    @Test
    public void shouldThrowEntityNotFoundExceptionWhenCourierNotFound() {
        Long courierId = 1L;
        Courier courierDto = buildCourier(courierId, "John Doe", true);

        when(repository.findById(courierId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.updateCourierById(courierDto, courierId));
    }

    private CourierEntity buildCourierEntity(Long id, String firstName, String lastName, boolean active) {
        return CourierEntity.builder()
                .id(id)
                .firstName(firstName)
                .lastName(lastName)
                .active(active)
                .build();
    }

    private Courier buildCourier(Long id, String name, boolean active) {
        return Courier.builder()
                .id(id)
                .name(name)
                .active(active)
                .build();
    }
}
