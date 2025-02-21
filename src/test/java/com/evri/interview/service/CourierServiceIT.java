package com.evri.interview.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.evri.interview.dto.CourierDto;
import com.evri.interview.model.Courier;

@SpringBootTest
@Transactional
class CourierServiceIT {

    @Autowired
    private CourierService courierService;

    @Test
    void getAllActiveCouriers() {
        List<Courier> activeCouriers = courierService.getAllCouriers(true);
        assertEquals(2, activeCouriers.size());
        assertEquals("Ben Askew", activeCouriers.get(0).getName());
        assertTrue(activeCouriers.get(0).isActive());
        assertEquals("John Doe", activeCouriers.get(1).getName());
        assertTrue(activeCouriers.get(1).isActive());
    }

    @Test
    void getAllCouriers() {
        List<Courier> allCouriers = courierService.getAllCouriers(false);
        assertEquals(3, allCouriers.size());
    }

    @Test
    void updateCourier_success() {
        CourierDto courierDto = new CourierDto("John", "Smith", true);
        Optional<Courier> updatedCourier = courierService.updateCourier(1L, courierDto);
        assertTrue(updatedCourier.isPresent());
        assertEquals("John Smith", updatedCourier.get().getName());
    }

    @Test
    void updateCourier_doesNOT_exist() {
        CourierDto courierDto = new CourierDto("John", "Smith", true);
        Optional<Courier> updatedCourier = courierService.updateCourier(4L, courierDto);
        assertFalse(updatedCourier.isPresent());
    }
}
