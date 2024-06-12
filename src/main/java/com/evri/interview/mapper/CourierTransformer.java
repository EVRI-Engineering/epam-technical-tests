package com.evri.interview.mapper;

import com.evri.interview.dto.CourierRequest;
import com.evri.interview.dto.CourierResponse;
import com.evri.interview.model.CourierEntity;
import org.springframework.stereotype.Component;

@Component
public class CourierTransformer {

    public CourierResponse toCourier(CourierEntity entity) {
        return CourierResponse.builder()
                .id(entity.getId())
                .name(String.format("%s %s", entity.getFirstName(), entity.getLastName()))
                .active(entity.isActive())
                .build();
    }

    public CourierEntity toCourierEntity(long id, CourierRequest request) {
        return CourierEntity.builder()
                .id(id)
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .active(request.getActive())
                .build();
    }

}
