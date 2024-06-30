package com.evri.interview.service;

import com.evri.interview.exception.InvalidNameFormatException;
import com.evri.interview.model.Courier;
import com.evri.interview.entity.CourierEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class CourierTransformerTest {

    @InjectMocks
    private CourierTransformer service;


    @Test
    void testToCourier() {
        //given
        CourierEntity courierEntity = getSimpleCourierEntity();

        //when
        Courier courier = service.toCourier(courierEntity);

        //then
        assertEquals(1L, courier.getId());
        assertEquals("John Doe", courier.getName());
        assertEquals(true, courier.getActive());
    }

    @Test
    void testToUpdatedCourierEntity_ValidName() {
        Courier newCourier = Courier.builder()
                .name("Alice Smith")
                .active(true)
                .build();
        CourierEntity courierEntity = getSimpleCourierEntity();

        CourierEntity updatedEntity = service.toUpdatedCourierEntity(courierEntity, newCourier);

        assertEquals("Alice", updatedEntity.getFirstName());
        assertEquals("Smith", updatedEntity.getLastName());
        assertTrue(updatedEntity.isActive());
    }

    @Test
    void testToUpdatedCourierEntity_InvalidNameFormat() {
        //given
        Courier newCourier = Courier.builder()
                .name("John")
                .build();
        CourierEntity courierEntity = getSimpleCourierEntity();

        //when
        Exception exception = assertThrows(InvalidNameFormatException.class, () -> {
            service.toUpdatedCourierEntity(courierEntity, newCourier);
        });

        //then
        assertEquals("Invalid format was used in provided name parameter: John", exception.getMessage());
    }

    @Test
    void testToUpdatedCourierEntity_NullName() {
        //given
        Courier newCourier = Courier.builder()
                .active(false)
                .build();
        CourierEntity courierEntity = getSimpleCourierEntity();

        //when
        CourierEntity updatedEntity = service.toUpdatedCourierEntity(courierEntity, newCourier);

        //then
        assertEquals(courierEntity.getFirstName(), updatedEntity.getFirstName());
        assertEquals(courierEntity.getLastName(), updatedEntity.getLastName());
        assertFalse(updatedEntity.isActive());
    }

    private static CourierEntity getSimpleCourierEntity() {
        return CourierEntity.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .active(true)
                .build();
    }
}
