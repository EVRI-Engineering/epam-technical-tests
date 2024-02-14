package com.evri.interview.service;

import com.evri.interview.exception.CourierValidationException;
import com.evri.interview.model.Courier;
import com.evri.interview.repository.CourierEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class CourierTransformer {
    private static final String SPACE = " ";
    private static final String REGEX = "^[A-Z][a-z]+\\s[A-Z][a-z]+$";

    public Courier toCourier(CourierEntity entity) {
        return Courier.builder()
            .id(entity.getId())
            .name(String.format("%s %s", entity.getFirstName(), entity.getLastName()))
            .active(entity.isActive())
            .build();
    }

    public List<Courier> toCourierList(List<CourierEntity> entityList) {
        return entityList.stream().map(this::toCourier).collect(Collectors.toList());
    }

    public CourierEntity toCourierEntity(Courier entity, long courierId) {
        if (!Pattern.matches(REGEX, entity.getName())) {
            throw new CourierValidationException(entity.getName());
        }
        String firstName = entity.getName().split(SPACE)[0];
        String lastName = entity.getName().split(SPACE)[1];

        return CourierEntity.builder()
            .id(courierId)
            .firstName(firstName)
            .lastName(lastName)
            .active(entity.isActive())
            .build();
    }
}
