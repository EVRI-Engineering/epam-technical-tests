package com.evri.interview.service;

import com.evri.interview.model.Courier;
import com.evri.interview.repository.CourierRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class CourierService {

    private CourierTransformer courierTransformer;
    private CourierRepository repository;

    public List<Courier> getAllCouriers(boolean isActive) {
    return repository.findAll().stream()
        .filter(courierEntity -> !isActive || courierEntity.isActive())
        .map(courierTransformer::toCourier)
        .collect(Collectors.toList());
    }

    @Transactional
    public Optional<Courier> updateCourier(long courierId, Courier updateCourierDetails) {
    return repository
        .findById(courierId)
        .map(
            courierEntity -> {
              courierEntity.setFirstName(updateCourierDetails.getFirstName());
              courierEntity.setLastName(updateCourierDetails.getLastName());
              courierEntity.setActive(updateCourierDetails.isActive());
              return courierTransformer.toCourier(repository.save(courierEntity));
            });
    }
}
