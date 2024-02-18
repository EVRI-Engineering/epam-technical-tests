package com.evri.interview.service;

import com.evri.interview.exception.CourierNotFoundException;
import com.evri.interview.model.Courier;
import com.evri.interview.repository.CourierEntity;
import com.evri.interview.repository.CourierRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CourierServiceTest {
    @Mock
    private CourierRepository repository;
    @Mock
    private CourierTransformer transformer;

    @InjectMocks
    private CourierService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldGetAllCouriersWhenIsActiveFalse() {
        CourierEntity firstCourier = CourierEntity.builder().id(1L).firstName("Harry").lastName("Potter").active(false).build();
        CourierEntity secondCourier = CourierEntity.builder().id(2L).firstName("Will").lastName("Smith").active(true).build();
        CourierEntity thirdCourier = CourierEntity.builder().id(3L).firstName("Brad").lastName("Pitt").active(true).build();
        List<CourierEntity> mockCourierList = Arrays.asList(firstCourier, secondCourier, thirdCourier);

        when(repository.findAll()).thenReturn(mockCourierList);
        doCallRealMethod().when(transformer).toCourier(any());

        List<Courier> result = service.getAllCouriers(false);

        assertEquals(3, result.size());
        assertEquals("Harry Potter", result.get(0).getName());
        assertEquals("Will Smith", result.get(1).getName());
        assertEquals("Brad Pitt", result.get(2).getName());
        verify(repository, times(1)).findAll();
    }

    @Test
    void shouldGetActiveCouriersWhenIsActiveTrue() {
        CourierEntity firstCourier = CourierEntity.builder().id(1L).firstName("Harry").lastName("Potter").active(false).build();
        CourierEntity secondCourier = CourierEntity.builder().id(2L).firstName("Will").lastName("Smith").active(true).build();
        CourierEntity thirdCourier = CourierEntity.builder().id(3L).firstName("Brad").lastName("Pitt").active(true).build();
        List<CourierEntity> mockCourierList = Arrays.asList(firstCourier, secondCourier, thirdCourier);

        when(repository.findAll()).thenReturn(mockCourierList);
        doCallRealMethod().when(transformer).toCourier(any());

        List<Courier> result = service.getAllCouriers(true);

        assertEquals(2, result.size());
        assertEquals("Will Smith", result.get(0).getName());
        assertEquals("Brad Pitt", result.get(1).getName());
        verify(repository, times(1)).findAll();
    }

    @Test
    void shouldUpdateCourierWhenCourierExistsById() {
        Courier courier = Courier.builder().id(1L).name("Brad Pitt").active(true).build();
        CourierEntity existedCourier = CourierEntity.builder().id(1L).firstName("Harry").lastName("Potter").active(true).build();

        when(repository.findById(1L)).thenReturn(Optional.of(existedCourier));
        doCallRealMethod().when(transformer).toCourierEntity(any(), any());

        service.updateCourierById(1L, courier);

        verify(repository, times(1)).findById(any());
        verify(repository, times(1)).save(any());
    }

    @Test
    void shouldThrowCourierNotFoundExceptionWhenCourierNotExistsById() {
        Courier courier = Courier.builder().id(1L).name("Brad Pitt").active(true).build();

        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CourierNotFoundException.class, () -> service.updateCourierById(1L, courier));
        verify(repository, times(1)).findById(any());
        verify(repository, times(0)).save(any());
    }
}
