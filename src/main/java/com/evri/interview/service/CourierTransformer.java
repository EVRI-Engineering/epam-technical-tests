package com.evri.interview.service;

import com.evri.interview.model.Courier;
import com.evri.interview.repository.CourierEntity;
import org.springframework.stereotype.Component;

@Component
public class CourierTransformer {

  private static final int SPACE_INDEX = ' ';

  public Courier toCourier(CourierEntity entity) {
    return Courier.builder()
        .id(entity.getId())
        .name(String.format("%s %s", entity.getFirstName(), entity.getLastName()))
        .active(entity.isActive())
        .build();
  }

  public void update(CourierEntity courierEntity, Courier courier) {
    String fullName = courier.getName();
    int idx = fullName.lastIndexOf(SPACE_INDEX);

    courierEntity.setFirstName(fullName.substring(0, idx));
    courierEntity.setLastName(fullName.substring(idx + 1));
    courierEntity.setActive(courier.getActive());
  }
}
