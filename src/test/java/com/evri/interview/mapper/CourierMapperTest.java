package com.evri.interview.mapper;

import com.evri.interview.dto.CourierResponseDto;
import com.evri.interview.dto.CourierUpdateDto;
import com.evri.interview.model.CourierEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CourierMapperTest {
    private final CourierMapper courierMapper = new CourierMapper();

    @Test
    public void testToCourierResponseDto() {
        CourierEntity entity = new CourierEntity(1L, "Joyce", "Barton", true);

        CourierResponseDto responseDto = courierMapper.toCourierResponseDto(entity);

        assertEquals(responseDto.getId(), 1L);
        assertEquals(responseDto.getName(), "Joyce Barton");
        assertTrue(responseDto.isActive());
    }

    @Test
    public void testUpdateCourierEntity() {
        CourierEntity entity = new CourierEntity(1L, "Joyce", "Barton", true);
        CourierUpdateDto updateDto = new CourierUpdateDto("Summer", "Hunter", false);

        courierMapper.updateCourierEntity(entity, updateDto);

        assertEquals(entity.getId(), 1L);
        assertEquals(entity.getFirstName(), "Summer");
        assertEquals(entity.getLastName(), "Hunter");
        assertFalse(entity.isActive());
    }
}
