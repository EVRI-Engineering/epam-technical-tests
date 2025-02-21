package com.evri.interview.service;

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

    public void updateCourierEntity(CourierEntity entity, CourierDto courierDto) {
        entity.setFirstName(courierDto.getFirstName());
        entity.setLastName(courierDto.getLastName());
        entity.setActive(courierDto.isActive());
    }
}
