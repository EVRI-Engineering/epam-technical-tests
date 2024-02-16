package com.evri.interview.service;

import com.evri.interview.exception.ResourceNotFoundException;
import com.evri.interview.model.Courier;
import com.evri.interview.model.UpdateCourier;
import com.evri.interview.repository.CourierEntity;
import com.evri.interview.repository.CourierRepository;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CourierService {

  private CourierTransformer courierTransformer;
  private CourierRepository repository;

  @Transactional(readOnly = true)
  public List<Courier> getAllCouriers(boolean isActive) {
    List<CourierEntity> courierEntities =
        isActive ? repository.findByActive(true) : repository.findAll();
    return courierEntities.stream().map(courierTransformer::toCourier).collect(Collectors.toList());
  }

  @Transactional
  public Courier updateCourier(long courierId, UpdateCourier courier) {
    CourierEntity courierEntity = getCourier(courierId);
    courierTransformer.updateCourierEntity(courierEntity, courier);
    CourierEntity updatedCourier = repository.save(courierEntity);
    return courierTransformer.toCourier(updatedCourier);
  }

  @Transactional(readOnly = true)
  public CourierEntity getCourier(long courierId) {
    return repository
        .findById(courierId)
        .orElseThrow(
            () ->
                new ResourceNotFoundException(
                    String.format("Courier not found, courierId: %s", courierId)));
  }
}
