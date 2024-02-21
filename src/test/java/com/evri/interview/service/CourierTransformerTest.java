package com.evri.interview.service;

import com.evri.interview.model.Courier;
import com.evri.interview.repository.CourierEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CourierTransformerTest {

    private CourierTransformer transformer;

    @BeforeEach
    void setUp() {
        transformer = new CourierTransformer();
    }

    @Test
    void shouldTransformToCourier() {
        CourierEntity entity = CourierEntity.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .active(true)
                .build();

        Courier result = transformer.toCourier(entity);

        assertEquals(1L, result.getId());
        assertEquals("John Doe", result.getName());
        assertTrue(result.isActive());
    }

    @Test
    void shouldTransformToCourierEntity() {
        Courier courier = Courier.builder()
                .id(2L)
                .name("Jane Doe")
                .active(false)
                .build();

        CourierEntity result = transformer.toCourierEntity(courier);

        assertEquals(2L, result.getId());
        assertEquals("Jane", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertFalse(result.isActive());
    }

    @Test
    void shouldTransformToCourierEntityWithSingleName() {
        Courier courier = Courier.builder()
                .id(3L)
                .name("SingleName")
                .active(true)
                .build();

        CourierEntity result = transformer.toCourierEntity(courier);

        assertEquals(3L, result.getId());
        assertEquals("SingleName", result.getFirstName());
        assertEquals("", result.getLastName());
        assertTrue(result.isActive());
    }
}
