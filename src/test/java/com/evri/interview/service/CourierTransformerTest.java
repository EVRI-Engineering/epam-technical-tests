package com.evri.interview.service;

import com.evri.interview.dto.CourierRequestDto;
import com.evri.interview.dto.CourierResponseDto;
import com.evri.interview.mapper.CourierTransformer;
import com.evri.interview.model.CourierEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CourierTransformerTest {

    private final CourierTransformer transformer = new CourierTransformer();

    @Test
    public void shouldTransformCourierEntityToCourier() {
        CourierEntity entity = CourierEntity.builder()
                .id(1)
                .active(true)
                .firstName("Jane")
                .lastName("Doe")
                .build();

        CourierResponseDto expected = CourierResponseDto.builder()
                .id(1)
                .name("Jane Doe")
                .active(true)
                .build();

        CourierResponseDto actual = transformer.toCourier(entity);

        assertEquals(expected, actual);
    }

    @Test
    public void shouldTransformCourierToCourierEntity() {
        CourierRequestDto courierRequestDto = new CourierRequestDto();
        courierRequestDto.setId(1);
        courierRequestDto.setFirstName("Jane");
        courierRequestDto.setLastName("Doe");
        courierRequestDto.setActive(true);

        CourierEntity expected = CourierEntity.builder()
                .id(1)
                .active(true)
                .firstName("Jane")
                .lastName("Doe")
                .build();


        CourierEntity actual = transformer.toCourierEntity(courierRequestDto);

        assertEquals(expected, actual);
    }

}