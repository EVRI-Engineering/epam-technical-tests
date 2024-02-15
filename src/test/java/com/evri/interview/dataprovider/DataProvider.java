package com.evri.interview.dataprovider;

import com.evri.interview.model.Courier;
import com.evri.interview.repository.CourierEntity;
import java.util.ArrayList;
import java.util.List;

public class DataProvider {

    private DataProvider() {
    }

    public static List<Courier> getCouriers() {
        List<Courier> couriers = new ArrayList<>();
        couriers.add(getCourier(1, "Ben Askew"));
        couriers.add(getCourier(2, "Andrii Okhrymovych"));
        return couriers;
    }

    public static List<CourierEntity> getCourierEntities() {
        List<CourierEntity> couriers = new ArrayList<>();
        couriers.add(getCourierEntity(1, "Ben", " Askew"));
        couriers.add(getCourierEntity(2, "Andrii", "Okhrymovych"));
        return couriers;
    }

    public static CourierEntity getCourierEntity(long id, String firstName, String lastName) {
        return CourierEntity.builder()
                .firstName(firstName)
                .lastName(lastName)
                .id(id)
                .active(true)
                .build();
    }

    public static Courier getCourier(long id, String name) {
        return Courier.builder()
                .name(name)
                .id(id)
                .active(true)
                .build();
    }
}
