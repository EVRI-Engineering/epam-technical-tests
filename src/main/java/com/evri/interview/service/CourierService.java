package com.evri.interview.service;

import com.evri.interview.exception.CourierNotFound;
import com.evri.interview.model.FullCourierDto;
import com.evri.interview.model.ShortCourierDto;
import com.evri.interview.repository.CourierEntity;
import com.evri.interview.repository.CourierRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CourierService {

    private CourierTransformer courierTransformer;
    private CourierRepository repository;

  public List<ShortCourierDto> getAllCouriers(boolean isActive) {
    return repository.findByActive(isActive)
        .stream().map(courierTransformer::toCourier)
                .collect(Collectors.toList());
    }

  public ShortCourierDto updateCourier(long courierId, FullCourierDto courierDto) {
    CourierEntity updatedCourierEntity = repository.findById(courierId)
        .map(courierEntity -> {
          courierEntity.setFirstName(courierDto.getFirstName());
          courierEntity.setLastName(courierDto.getLastName());
          courierEntity.setActive(courierDto.isActive());
          return repository.save(courierEntity);
        })
        .orElseThrow(() -> new CourierNotFound(
            String.format("Courier with id: %s has not been found", courierId)));
    return courierTransformer.toCourier(updatedCourierEntity);
  }

}
