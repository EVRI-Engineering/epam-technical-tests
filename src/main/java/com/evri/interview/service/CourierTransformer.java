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
        String name = courier.getName();
        String firstName = "";
        String lastName = "";
        int spaceIndex = name.indexOf(" ");

        if (spaceIndex != -1) {
            firstName = name.substring(0, spaceIndex);
            lastName = name.substring(spaceIndex + 1);
        } else {
            firstName = name;
        }

        return CourierEntity.builder()
                .id(courier.getId())
                .firstName(firstName)
                .lastName(lastName)
                .active(courier.isActive())
                .build();
    }
}
