package com.evri.interview.mapper;

import com.evri.interview.dto.CourierDto;
import com.evri.interview.model.Courier;
import com.evri.interview.repository.CourierEntity;
import org.springframework.stereotype.Component;

@Component
public class CourierTransformer {

    public Courier toCourier(CourierEntity entity) {
        return Courier.builder()
                .id(entity.getId())
                .name(String.format("%s %s", entity.getFirstName(), entity.getLastName()))
                .active(entity.isActive())
                .build();
    }

    public CourierEntity toCourierEntity(Long id, CourierDto dto) {
        return CourierEntity.builder()
                .id(id)
                .firstName(dto.getName())
                .lastName(dto.getLastName())
                .active(dto.isActive())
                .build();
    }

}
