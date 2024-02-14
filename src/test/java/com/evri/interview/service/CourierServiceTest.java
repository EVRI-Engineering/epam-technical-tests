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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CourierServiceTest {
    @Mock
    private CourierTransformer courierTransformer;

    @Mock
    private CourierRepository repository;

    @InjectMocks
    private CourierService courierService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllCouriersOrActive() {
        Courier firstCourier = Courier.builder().id(1L).name("John Doe").active(true).build();
        Courier secondCourier = Courier.builder().id(2L).name("Jane Smith").active(true).build();
        List<Courier> mockCourierList = List.of(firstCourier, secondCourier);

        when(courierTransformer.toCourierList(repository.findByActive(true))).thenReturn(mockCourierList);

        courierService = new CourierService(courierTransformer, repository);

        List<Courier> result = courierService.getAllCouriersOrActive(true);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("John Doe", result.get(0).getName());
        assertEquals("Jane Smith", result.get(1).getName());
    }

    @Test
    void testUpdateCourierById_ExistingCourier() {
        long courierId = 1;
        Courier updatedCourier = Courier.builder().id(1L).name("Steve Smith").active(true).build();
        CourierEntity existingCourier =
            CourierEntity.builder().id(1L).firstName("Jane").lastName("Smith").active(true).build();

        when(repository.findById(courierId)).thenReturn(Optional.of(existingCourier));
        when(courierTransformer.toCourierEntity(updatedCourier, courierId)).thenReturn(existingCourier);

        courierService.updateCourierById(updatedCourier, courierId);

        verify(repository, times(1)).findById(courierId);
        verify(repository, times(1)).save(existingCourier);
    }

    @Test
    void testUpdateCourierById_NonExistingCourier() {
        long courierId = 1;
        Courier updatedCourier = Courier.builder().id(1L).name("Updated Courier").active(true).build();

        when(repository.findById(courierId)).thenReturn(Optional.empty());

        assertThrows(
            CourierNotFoundException.class,
            () -> {
                courierService.updateCourierById(updatedCourier, courierId);
            });

        verify(repository, times(1)).findById(courierId);
        verify(repository, never()).save(any());
    }
}
