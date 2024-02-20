package com.evri.interview.service;

import com.evri.interview.converter.CourierConverter;
import com.evri.interview.model.Courier;
import com.evri.interview.repository.CourierEntity;
import org.junit.jupiter.api.Test;

import static com.evri.interview.constants.PresetObjects.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CourierConverterTest {

    private final CourierConverter sut = CourierConverter.INSTANCE;

    @Test
    void shouldConvertToDto() {
        //When
        Courier actual = sut.entityToCourier(ACTIVE_ENTITY);

        //Then
        assertEquals(ACTIVE_DTO, actual);
    }

    @Test
    void shouldConvertToEntity() {
        //When
        CourierEntity actual = sut.updateEntity(ACTIVE_ENTITY, UPDATE_DTO);

        //Then
        assertEquals(1, actual.getId());
        assertEquals(UPDATE_DTO.getFirstName(), actual.getFirstName());
        assertEquals(UPDATE_DTO.getLastName(), actual.getLastName());
        assertEquals(UPDATE_DTO.getActive(), actual.isActive());
    }

}