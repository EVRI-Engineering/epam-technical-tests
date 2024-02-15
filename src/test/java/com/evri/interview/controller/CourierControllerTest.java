package com.evri.interview.controller;

import com.evri.interview.model.Courier;
import com.evri.interview.repository.CourierEntity;
import com.evri.interview.service.CourierService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CourierControllerTest {
    @Mock
    CourierService courierService;
    @InjectMocks
    CourierController controller;

    @Test
    void getAllCouriers() {
        ArrayList<Courier> couriers = new ArrayList<>();
        couriers.add(mock(Courier.class));
        couriers.add(mock(Courier.class));
        when(courierService.getAllCouriers(false)).thenReturn(couriers);

        ResponseEntity<List<Courier>> allCouriers = controller.getAllCouriers(false);

        assertEquals(HttpStatus.OK, allCouriers.getStatusCode());
        assertNotNull(allCouriers.getBody());
        assertEquals(2, allCouriers.getBody().size());
    }

    @Test
    void updateCourier_notFound() {
        when(courierService.findById(anyLong())).thenReturn(Optional.empty());

        ResponseEntity<Courier> response = controller.updateCourier(1L, Courier.builder().build());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
    @Test
    void updateCourier() {
        Courier courier = Courier.builder()
                .id(1L)
                .name("Max Liskov")
                .active(true)
                .build();
        when(courierService.findById(anyLong())).thenReturn(Optional.of(mock(CourierEntity.class)));
        when(courierService.update(any())).thenReturn(courier);

        ResponseEntity<Courier> response = controller.updateCourier(1L, courier);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(response.getBody(), courier);
    }
}