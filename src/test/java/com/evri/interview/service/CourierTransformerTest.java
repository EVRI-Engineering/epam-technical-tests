package com.evri.interview.service;

import com.evri.interview.model.Courier;
import com.evri.interview.repository.CourierEntity;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.evri.interview.config.ConstantsAndVariables.WRONG_NAME;
import static org.junit.jupiter.api.Assertions.*;

public class CourierTransformerTest {
    private static CourierTransformer transformer;

    @BeforeAll
    static void setup() {
        transformer = new CourierTransformer();
    }

    @Test
    @DisplayName("Transform to  courier")
    void transformToCourier_ShouldReturnValidCourier() {
        CourierEntity entity = CourierEntity.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .active(true)
                .build();

        Courier courier = transformer.transformToCourier(entity);

        assertNotNull(courier);
        assertEquals(1L, courier.getId());
        assertEquals("John Doe", courier.getName());
        assertTrue(courier.isActive());
    }

    @Test
    @DisplayName("Transform to entity")
    void transformToEntity_ShouldReturnValidCourierEntity() {
        Courier courier = Courier.builder()
                .id(2L)
                .name("Jane Doe")
                .active(false)
                .build();

        CourierEntity entity = transformer.transformToEntity(courier);

        assertNotNull(entity);
        assertEquals(2L, entity.getId());
        assertEquals("Jane", entity.getFirstName());
        assertEquals("Doe", entity.getLastName());
        assertFalse(entity.isActive());
    }

    @Test
    @DisplayName("Transform to entity with exception")
    void transformToEntity_ShouldThrowException_WhenNameIsInvalid() {
        Courier courier = Courier.builder()
                .id(3L)
                .name("InvalidName")
                .active(true)
                .build();

        IllegalStateException exception = assertThrows(IllegalStateException.class, () ->
                transformer.transformToEntity(courier)
        );

        assertEquals(WRONG_NAME, exception.getMessage());
    }
}