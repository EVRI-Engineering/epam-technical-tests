package com.evri.interview.service;

import com.evri.interview.controller.dto.CourierDto;
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

    public void merge(CourierEntity entity, CourierDto dto) {
        String[] name = dto.getName().split(" ");
        entity.setFirstName(name[0]);
        entity.setLastName(name[1]);
        entity.setActive(dto.isActive());
    }

}
