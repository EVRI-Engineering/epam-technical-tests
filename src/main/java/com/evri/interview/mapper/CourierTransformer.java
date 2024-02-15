package com.evri.interview.mapper;

import com.evri.interview.dto.CourierRequestDto;
import com.evri.interview.dto.CourierResponseDto;
import com.evri.interview.model.CourierEntity;
import org.springframework.stereotype.Component;

@Component
public class CourierTransformer {

    public CourierResponseDto toCourier(CourierEntity entity) {
        return CourierResponseDto.builder()
                .id(entity.getId())
                .name(String.format("%s %s", entity.getFirstName(), entity.getLastName()))
                .active(entity.isActive())
                .build();
    }

    public CourierEntity toCourierEntity(CourierRequestDto courierRequestDto) {
        return CourierEntity.builder()
                .id(courierRequestDto.getId())
                .firstName(courierRequestDto.getFirstName())
                .lastName(courierRequestDto.getLastName())
                .active(courierRequestDto.isActive())
                .build();
    }

}
