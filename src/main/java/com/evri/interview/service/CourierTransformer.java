package com.evri.interview.service;

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

    public CourierEntity toCourierEntity(Courier courier) {
        return CourierEntity.builder()
                .id(courier.getId())
                .firstName(courier.getName().split("\\s+")[0])
                .lastName(courier.getName().split("\\s+")[1])
                .active(courier.isActive())
                .build();
    }
}
