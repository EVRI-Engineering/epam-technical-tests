package com.evri.interview.mapper;

import com.evri.interview.dto.CourierResponseDto;
import com.evri.interview.dto.CourierUpdateDto;
import com.evri.interview.model.CourierEntity;
import org.springframework.stereotype.Component;

@Component
public class CourierMapper {

    public CourierResponseDto toCourierResponseDto(CourierEntity entity) {
        return CourierResponseDto.builder()
                .id(entity.getId())
                .name(String.format("%s %s", entity.getFirstName(), entity.getLastName()))
                .active(entity.isActive())
                .build();
    }

    public void updateCourierEntity(CourierEntity entity, CourierUpdateDto dto) {
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setActive(dto.getActive());
    }

}
