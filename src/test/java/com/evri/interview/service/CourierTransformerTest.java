package com.evri.interview.service;

import com.evri.interview.exception.CourierValidationException;
import com.evri.interview.model.Courier;
import com.evri.interview.repository.CourierEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CourierTransformerTest {
    private CourierTransformer courierTransformer;

    @BeforeEach
    void setUp() {
        courierTransformer = new CourierTransformer();
    }

    @Test
    void toCourier() {
        CourierEntity entity = CourierEntity.builder()
            .id(1L)
            .firstName("John")
            .lastName("Doe")
            .active(true)
            .build();

        Courier courier = courierTransformer.toCourier(entity);

        assertNotNull(courier);
        assertEquals(1L, courier.getId());
        assertEquals("John Doe", courier.getName());
        assertTrue(courier.isActive());
    }

    @Test
    void toCourierList() {
        List<CourierEntity> entityList = Arrays.asList(
            CourierEntity.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .active(true)
                .build(),
            CourierEntity.builder()
                .id(2L)
                .firstName("Jane")
                .lastName("Smith")
                .active(false)
                .build()
        );

        List<Courier> couriers = courierTransformer.toCourierList(entityList);

        assertNotNull(couriers);
        assertEquals(2, couriers.size());
        assertEquals("John Doe", couriers.get(0).getName());
        assertEquals("Jane Smith", couriers.get(1).getName());
        assertTrue(couriers.get(0).isActive());
        assertFalse(couriers.get(1).isActive());
    }

    @Test
    void toCourierEntity() {
        Courier courier = Courier.builder()
            .id(1L)
            .name("John Doe")
            .active(true)
            .build();

        CourierEntity entity = courierTransformer.toCourierEntity(courier, 1L);

        assertNotNull(entity);
        assertEquals(1L, entity.getId());
        assertEquals("John", entity.getFirstName());
        assertEquals("Doe", entity.getLastName());
        assertTrue(entity.isActive());
    }

    @Test
    void toCourierEntity_InvalidName() {
        Courier courier = Courier.builder()
            .id(1L)
            .name("JohnDoe")
            .active(true)
            .build();

        assertThrows(CourierValidationException.class,
            () -> courierTransformer.toCourierEntity(courier, 1L));
    }
}
