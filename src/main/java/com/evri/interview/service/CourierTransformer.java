package com.evri.interview.service;

import com.evri.interview.model.ShortCourierDto;
import com.evri.interview.repository.CourierEntity;
import org.springframework.stereotype.Component;

@Component
public class CourierTransformer {

    public ShortCourierDto toCourier(CourierEntity entity) {
        return ShortCourierDto.builder()
                .id(entity.getId())
                .name(String.format("%s %s", entity.getFirstName(), entity.getLastName()))
                .active(entity.isActive())
                .build();
    }

}
