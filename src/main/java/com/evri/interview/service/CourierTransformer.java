package com.evri.interview.service;

import com.evri.interview.model.Courier;
import org.springframework.stereotype.Component;

@Component
public class CourierTransformer {

    public Courier toCourier(Courier entity) {
        return Courier.builder()
                .id(entity.getId()).firstName(entity.getFirstName()).lastName(entity.getLastName())
                .active(entity.isActive())
                .build();
    }
}