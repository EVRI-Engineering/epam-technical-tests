package com.evri.interview.service;

import com.evri.interview.model.Courier;
import com.evri.interview.repository.CourierEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

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
        String firstName = "";
        String lastName = "";
        String name = courier.getName();
        if (StringUtils.hasText(name)) {
            String[] splitName = name.split(" ");
            firstName = splitName[0];
            lastName = splitName[1];
        }

        return CourierEntity.builder()
                .id(courier.getId())
                .firstName(firstName)
                .lastName(lastName)
                .active(courier.isActive())
                .build();
    }
}
