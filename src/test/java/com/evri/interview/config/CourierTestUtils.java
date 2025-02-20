package com.evri.interview.config;

import com.evri.interview.model.Courier;
import com.evri.interview.repository.CourierEntity;


public class CourierTestUtils {
    public static final String API_URL = "http://localhost:";
    public static final String COURIERS_API = "/api/couriers";

    public static final Long FIRST_COURIER_ID = 1L;
    public static final Long SECOND_COURIER_ID = 2L;

    public static final String COURIER_FIRST_NAME = "Donald";
    public static final String COURIER_LAST_NAME = "Trump";

    public static final String SECOND_COURIER_FIRST_NAME = "Bogdan";
    public static final String SECOND_COURIER_LAST_NAME = "Khmelnitsky";
    public static final long INVALID_ID = -1;

    public static Courier courier() {
        return Courier.builder().id(FIRST_COURIER_ID)
                .name(COURIER_FIRST_NAME + " " + COURIER_LAST_NAME)
                .active(true)
                .build();
    }


    public static CourierEntity courierEntity() {
        return CourierEntity.builder().id(FIRST_COURIER_ID)
                .firstName(COURIER_FIRST_NAME)
                .lastName(COURIER_LAST_NAME)
                .active(true)
                .build();
    }

    public static Courier inactiveCourier() {
        return Courier.builder().id(SECOND_COURIER_ID)
                .name(SECOND_COURIER_FIRST_NAME + " " + SECOND_COURIER_LAST_NAME)
                .active(false)
                .build();
    }

    public static CourierEntity inactiveCourierEntity() {
        return CourierEntity.builder().id(SECOND_COURIER_ID)
                .firstName(SECOND_COURIER_FIRST_NAME)
                .lastName(SECOND_COURIER_LAST_NAME)
                .active(false)
                .build();
    }

    public static CourierEntity testTransformToCourierEntity() {
        return CourierEntity.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .active(true)
                .build();

    }

    public static Courier testTransformToEntityCourier() {
        return Courier.builder().id(2L).name("Jane Doe").active(false).build();
    }

    public static Courier testTransformToEntityWithInvalidNameCourier() {
        return Courier.builder()
                .id(3L)
                .name("InvalidName")
                .active(true)
                .build();
    }
}