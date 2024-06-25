package com.evri.interview.service;

import com.evri.interview.model.CourierDTO;
import com.evri.interview.repository.CourierEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class CourierTransformerTest {
    @InjectMocks
    CourierTransformer courierTransformer;

    @Test
    void toCourierEntityTransformsEntityProperly(){
        final String firstName = "First";
        final String lastName = "Last";
        final boolean active = true;
        final long id = 12L;
        final CourierDTO dto = CourierDTO.builder()
                .firstName(firstName)
                .lastName(lastName)
                .active(true)
                .build();

        CourierEntity result = courierTransformer.toCourierEntity(id, dto);

        assertEquals(id, result.getId());
        assertEquals(firstName, result.getFirstName());
        assertEquals(lastName, result.getLastName());
        assertEquals(active, result.isActive());
    }
}
