package com.evri.interview.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import com.evri.interview.model.Courier;
import com.evri.interview.repository.CourierEntity;

import static org.junit.jupiter.api.Assertions.*;

class CourierTransformerTest {
    @Test
    @DisplayName("test transforming courier entity to courier")
    void testToCourier() {
        CourierTransformer transformer = new CourierTransformer();
        CourierEntity courierEntity = new CourierEntity(1, "Ben", "Askew", true);

        Courier courier = transformer.toCourier(courierEntity);

        assertEquals(1, courier.getId());
        assertEquals("Ben Askew", courier.getName());
        assertTrue(courier.isActive());
    }

}