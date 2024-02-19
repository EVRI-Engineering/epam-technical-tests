package com.evri.interview.controller;

import com.evri.interview.model.Courier;
import com.evri.interview.model.UpdateCourier;
import com.evri.interview.service.CourierService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CourierControllerTest {

    @Mock
    private CourierService courierService;

    @InjectMocks
    private CourierController courierController;

    @Test
    @DisplayName("test getting all only active couriers")
    public void testGetAllCouriers() {
        boolean isActive = false;
        List<Courier> couriers = Arrays.asList(mock(Courier.class), mock(Courier.class));
        when(courierService.getAllCouriers(isActive)).thenReturn(couriers);

        ResponseEntity<List<Courier>> allCouriersResponse = courierController.getAllCouriers(isActive);

        verify(courierService, times(1)).getAllCouriers(isActive);
        assertEquals(HttpStatus.OK, allCouriersResponse.getStatusCode());
        assertNotNull(allCouriersResponse.getBody());
        assertEquals(2, allCouriersResponse.getBody().size());
    }

    @Test
    @DisplayName("test updating courier")
    void testUpdateCourier() {
        long courierId = 1;
        Courier expectedCourier = Courier.builder()
                .id(courierId)
                .name("Ben Askew")
                .active(true)
                .build();
        UpdateCourier mockedUpdateCourier = mock(UpdateCourier.class);
        when(courierService.updateCourierById(courierId, mockedUpdateCourier))
                .thenReturn(Optional.of(expectedCourier));

        ResponseEntity<Courier> courierResponse = courierController.updateCourier(1L, mockedUpdateCourier);
        assertEquals(HttpStatus.OK, courierResponse.getStatusCode());
        assertNotNull(courierResponse.getBody());
        assertEquals(expectedCourier, courierResponse.getBody());
    }

    @Test
    @DisplayName("test updating non-existing courier")
    public void testUpdateCourierNotFound() {
        UpdateCourier updateCourier = new UpdateCourier("Ben", "1", true);
        when(courierService.updateCourierById(1L, updateCourier))
                .thenReturn(Optional.empty());

        ResponseEntity<Courier> updatedCourierResponse = courierController.updateCourier(1L, updateCourier);
        assertEquals(HttpStatus.NOT_FOUND, updatedCourierResponse.getStatusCode());
        assertNull(updatedCourierResponse.getBody());
    }

    private Courier createCourier(long id, String name, boolean isActive) {
        return Courier.builder()
                .id(id)
                .name(name)
                .active(isActive)
                .build();
    }
}