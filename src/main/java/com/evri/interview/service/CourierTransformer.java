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
        String[] names = courier.getName().split(" ");
        return CourierEntity.builder()
                .id(courier.getId())
                .firstName(names.length > 2 ? names[0] : null)
                .lastName(names.length > 2 ? names[1] : null)
                .active(courier.isActive())
                .build();
    }
}
