package com.evri.interview.service;

import com.evri.interview.model.Courier;
import com.evri.interview.model.UpdateCourier;
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

  public void updateCourierEntity(CourierEntity entity, UpdateCourier courier) {
    entity.setFirstName(courier.getFirstName());
    entity.setLastName(courier.getLastName());
    entity.setActive(courier.getActive());
  }
}
