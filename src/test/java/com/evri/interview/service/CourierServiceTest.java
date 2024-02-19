package com.evri.interview.service;


import com.evri.interview.model.Courier;
import com.evri.interview.model.UpdateCourier;
import com.evri.interview.repository.CourierEntity;
import com.evri.interview.repository.CourierRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static java.util.Collections.singletonList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CourierServiceTest {

    @Mock
    private CourierRepository courierRepository;

    @Mock
    private CourierTransformer courierTransformer;

    @InjectMocks
    private CourierService courierService;

    @BeforeEach
    public void setup() {
        lenient().when(courierTransformer.toCourier(any(CourierEntity.class))).thenCallRealMethod();
    }

    @Test
    @DisplayName("test getting all couriers")
    public void testGetAllCouriers() {
        List<CourierEntity> mockedEntities = singletonList(mock(CourierEntity.class));
        when(courierRepository.findAll()).thenReturn(mockedEntities);

        List<Courier> couriers = courierService.getAllCouriers(false);

        verify(courierRepository).findAll();
        assertFalse(couriers.isEmpty());
    }

    @Test
    @DisplayName("test getting all only active couriers")
    public void testGetAllCouriersIsActiveTrue() {
        List<CourierEntity> mockedEntities = singletonList(mock(CourierEntity.class));
        when(courierRepository.findByActiveTrue()).thenReturn(mockedEntities);

        List<Courier> couriers = courierService.getAllCouriers(true);

        verify(courierRepository).findByActiveTrue();
        assertFalse(couriers.isEmpty());
    }

    @Test
    @DisplayName("test updating courier by id")
    public void testUpdateCourierById() {
        CourierEntity mockedEntity = mock(CourierEntity.class);
        when(courierRepository.findById(anyLong())).thenReturn(Optional.of(mockedEntity));
        when(courierRepository.save(any(CourierEntity.class))).thenReturn(mockedEntity);

        UpdateCourier updateCourier = new UpdateCourier("Ben", "Askew", true);
        Optional<Courier> updatedCourier = courierService.updateCourierById(1L,updateCourier);

        verify(courierRepository, times(1)).findById(anyLong());
        verify(mockedEntity, times(1)).setFirstName(updateCourier.getFirstName());
        verify(mockedEntity, times(1)).setLastName(updateCourier.getLastName());
        verify(mockedEntity, times(1)).setActive(updateCourier.getActive());
        verify(courierRepository, times(1)).save(any(CourierEntity.class));
        assertTrue(updatedCourier.isPresent());
    }

    @Test
    @DisplayName("test updating non-existing courier")
    public void testUpdateCourierByIdNotFound() {
        when(courierRepository.findById(anyLong())).thenReturn(Optional.empty());

        Optional<Courier> updatedCourier = courierService.updateCourierById(1L, mock(UpdateCourier.class));

        verify(courierRepository, times(1)).findById(anyLong());
        assertFalse(updatedCourier.isPresent());
    }
}
