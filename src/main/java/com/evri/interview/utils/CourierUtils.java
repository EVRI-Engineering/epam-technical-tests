package com.evri.interview.utils;

import com.evri.interview.model.Courier;
import com.evri.interview.repository.CourierEntity;

public class CourierUtils {
    public static final Long COURIER_ID = 1L;
    public static final String COURIER_FIRST_NAME = "Ben";
    public static final String COURIER_LAST_NAME = "Askew";
    public static final boolean ACTIVE = true;
    public static final Long SECOND_COURIER_ID = 2L;
    public static final String SECOND_COURIER_FIRST_NAME = "Ivan";
    public static final String SECOND_COURIER_LAST_NAME = "Melnyk";

    public static Courier courier() {
        return Courier.builder()
                .id(COURIER_ID)
                .name(COURIER_FIRST_NAME + " " + COURIER_LAST_NAME)
                .active(ACTIVE)
                .build();
    }

    public static Courier updatedCourier() {
        return Courier.builder()
                .id(COURIER_ID)
                .name(SECOND_COURIER_FIRST_NAME + " " + SECOND_COURIER_LAST_NAME)
                .active(ACTIVE)
                .build();
    }

    public static Courier inactiveCourier() {
        return Courier.builder()
                .id(SECOND_COURIER_ID)
                .name(SECOND_COURIER_FIRST_NAME + " " + SECOND_COURIER_LAST_NAME)
                .active(false)
                .build();
    }

    public static CourierEntity courierEntity() {
        return CourierEntity.builder()
                .id(COURIER_ID)
                .firstName(COURIER_FIRST_NAME)
                .lastName(COURIER_LAST_NAME)
                .active(ACTIVE)
                .build();
    }

    public static CourierEntity inactiveCourierEntity() {
        return CourierEntity.builder()
                .id(SECOND_COURIER_ID)
                .firstName(SECOND_COURIER_FIRST_NAME)
                .lastName(SECOND_COURIER_LAST_NAME)
                .active(false)
                .build();
    }
}