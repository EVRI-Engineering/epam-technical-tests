package com.evri.interview.service;

import com.evri.interview.model.Courier;
import com.evri.interview.repository.CourierEntity;
import org.springframework.stereotype.Component;

@Component
public class CourierTransformer {
    final int FIRST_CHARACTER_INDEX = 0;
    final int OFFSET_AFTER_SPACE = 1;
    final int NO_SPACE_FOUND = -1;

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

        boolean hasSpace = spaceIndex != NO_SPACE_FOUND;
        if (hasSpace) {
            firstName = name.substring(FIRST_CHARACTER_INDEX, spaceIndex);
            lastName = name.substring(spaceIndex + OFFSET_AFTER_SPACE);
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
