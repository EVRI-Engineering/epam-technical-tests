package com.evri.interview.service;

import com.evri.interview.model.Courier;
import com.evri.interview.repository.CourierEntity;
import org.springframework.stereotype.Component;

@Component
public class CourierTransformer {

    private static final String DELIMITER = " ";

    public Courier toCourier(CourierEntity entity) {
        return Courier.builder()
                .id(entity.getId())
                .name(String.format("%s %s", entity.getFirstName(), entity.getLastName()))
                .active(entity.isActive())
                .build();
    }

    public CourierEntity toCourierEntity(Long id, Courier data) {
        String name = data.getName();
        String firstName = name.split(DELIMITER)[0];
        String lastName = name.split(DELIMITER)[1];

        return CourierEntity.builder()
                .id(id)
                .firstName(firstName)
                .lastName(lastName)
                .active(data.isActive())
                .build();
    }
}

