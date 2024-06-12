package com.evri.interview.util;

import com.evri.interview.dto.CourierRequest;
import com.evri.interview.dto.CourierResponse;
import com.evri.interview.model.CourierEntity;

public final class DataProvider {

    private DataProvider() {
        throw new UnsupportedOperationException("It is forbidden to create an instance of the class.");
    }

    public static CourierEntity buildCourierEntity(int id, String firstName, String lastName, Boolean active) {
        return CourierEntity.builder()
                .id(id)
                .firstName(firstName)
                .lastName(lastName)
                .active(active)
                .build();
    }

    public static CourierResponse buildCourierResponse(long id, String name, Boolean active) {
        return CourierResponse
                .builder()
                .id(id)
                .name(name)
                .active(active)
                .build();
    }

    public static CourierRequest buildCourierRequest(String firstName, String lastName, Boolean active) {
        return CourierRequest
                .builder()
                .firstName(firstName)
                .lastName(lastName)
                .active(active)
                .build();
    }
}
