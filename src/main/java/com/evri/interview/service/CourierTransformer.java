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

    public CourierEntity toCourierEntity(Courier entity, Long courierId) {
        String[] nameParts = entity.getName().split("\\s+");

        return CourierEntity.builder()
                .id(courierId)
                .firstName(nameParts[0])
                .lastName(nameParts[1])
                .active(entity.isActive())
                .build();
    }

}
