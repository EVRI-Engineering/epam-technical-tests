package com.evri.interview.constants;

import com.evri.interview.model.Courier;
import com.evri.interview.model.CourierForUpdate;
import com.evri.interview.repository.CourierEntity;

public interface PresetObjects {

    CourierEntity ACTIVE_ENTITY = CourierEntity.builder()
            .id(1)
            .firstName("ActiveFName")
            .lastName("ActiveLName")
            .active(true)
            .build();
    CourierEntity INACTIVE_ENTITY = CourierEntity.builder()
            .id(2)
            .firstName("InactiveFName")
            .lastName("InactiveLName")
            .active(false)
            .build();

    Courier ACTIVE_DTO = Courier.builder()
            .id(1)
            .name("ActiveFName ActiveLName")
            .active(true)
            .build();
    Courier INACTIVE_DTO = Courier.builder()
            .id(2)
            .name("InactiveFName InactiveLName")
            .active(false)
            .build();

    CourierForUpdate UPDATE_DTO = CourierForUpdate.builder()
            .firstName("UpdatedFName")
            .lastName("UpdatedLName")
            .active(false)
            .build();

}
