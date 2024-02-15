package com.evri.interview.service;

import com.evri.interview.model.Courier;
import com.evri.interview.repository.CourierEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CourierTransformerTest {

    CourierTransformer transformer = new CourierTransformer();

    @Test
    void toCourier() {
        CourierEntity courierEntity = CourierEntity.builder()
                .id(1L)
                .firstName("Bob")
                .lastName("Liskov")
                .active(true)
                .build();

        Courier courier = transformer.toCourier(courierEntity);

        assertions(courier, courierEntity);
    }

    @Test
    void toCourierEntity() {
        Courier courier = Courier.builder()
                .id(1L)
                .name("Bob Liskov")
                .active(true)
                .build();

        CourierEntity courierEntity = transformer.toCourierEntity(courier);

        assertions(courier, courierEntity);
    }

    private static void assertions(Courier courier, CourierEntity courierEntity) {
        assertEquals(courier.getId(), courierEntity.getId());
        assertEquals(courier.getName(), String.format("%s %s", courierEntity.getFirstName(), courierEntity.getLastName()));
        assertEquals(courier.isActive(), courierEntity.isActive());
    }
}