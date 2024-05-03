package com.evri.interview.service;

import com.evri.interview.model.Courier;
import com.evri.interview.model.UpdateCourierRequest;
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

    public void updateCourier(CourierEntity courierEntity, final UpdateCourierRequest updateCourierRequest) {
        final String[] name = updateCourierRequest.getName().split(" ");
        courierEntity.setFirstName(name[0]);
        courierEntity.setLastName(name[1]);
        courierEntity.setActive(updateCourierRequest.isActive());
    }

}
