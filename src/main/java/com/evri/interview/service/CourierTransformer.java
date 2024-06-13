package com.evri.interview.service;

import org.springframework.stereotype.Component;

import com.evri.interview.model.Courier;
import com.evri.interview.repository.CourierEntity;

@Component
public class CourierTransformer {

    public Courier toCourier(CourierEntity entity) {
        return Courier.builder()
                .id(entity.getId())
                .name(String.format("%s %s", entity.getFirstName(), entity.getLastName()))
                .active(entity.isActive())
                .build();
    }

    public CourierEntity toCourierEntity(final Courier courier) {
        final FirstAndLastNames firstAndLastNames = resolveNames(courier.getName());
        return CourierEntity.builder()
          .firstName(firstAndLastNames.firstName())
          .lastName(firstAndLastNames.lastName())
          .active(courier.isActive())
          .build();
    }

    private FirstAndLastNames resolveNames(final String fullName) {
        final String[] names = fullName.split(" ");
        return new FirstAndLastNames(names.length > 0 ? names[0] : null, names.length > 1 ? names[1] : null);
    }

    record FirstAndLastNames(String firstName, String lastName) {

    }
}
