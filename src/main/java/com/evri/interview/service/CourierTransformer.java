package com.evri.interview.service;

import com.evri.interview.model.Courier;
import com.evri.interview.model.CourierPutDataModel;
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

    public CourierEntity toEntity(final Long courierId, final CourierPutDataModel courierPutDataModel) {
        return CourierEntity.builder()
            .id(courierId)
            .firstName(courierPutDataModel.getFirstName())
            .lastName(courierPutDataModel.getLastName())
            .active(courierPutDataModel.isActive())
            .build();
    }

}
