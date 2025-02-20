package com.evri.interview.service;

import com.evri.interview.model.Courier;
import com.evri.interview.repository.CourierEntity;
import org.springframework.stereotype.Component;

import static com.evri.interview.config.ConstantsAndVariables.EMPTY;
import static com.evri.interview.config.ConstantsAndVariables.WRONG_NAME;

@Component
public class CourierTransformer {

    public Courier transformToCourier(CourierEntity entity) {
        return Courier.builder()
                .id(entity.getId())
                .name(String.format("%s %s", entity.getFirstName(), entity.getLastName()))
                .active(entity.isActive())
                .build();
    }

    public CourierEntity transformToEntity(Courier courier) {
        if (courier.getName().trim().split("\\s+").length != 2) {
            throw new IllegalStateException(WRONG_NAME);
        }
        String[] courierName = courier.getName().split(EMPTY);
        return CourierEntity.builder()
                .id(courier.getId())
                .firstName(courierName[0])
                .lastName(courierName[1])
                .active(courier.isActive())
                .build();
    }

}
