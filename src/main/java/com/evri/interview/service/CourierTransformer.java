package com.evri.interview.service;

import com.evri.interview.model.Courier;
import com.evri.interview.repository.CourierEntity;
import org.springframework.stereotype.Component;

@Component
public class CourierTransformer {

    public Courier toCourier(CourierEntity entity) {
        return new Courier()
                .setId(entity.getId())
                .setName(String.format("%s %s", entity.getFirstName(), entity.getLastName()))
                .setActive(entity.isActive());
    }

    public CourierEntity toCourierEntity(Long id, Courier courier) {
        String[] name = courier.getName().split(" ");

        return new CourierEntity()
                .setId(id)
                .setFirstName(name[0])
                .setLastName(name[1])
                .setActive(courier.isActive());
    }
}
