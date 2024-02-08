package com.evri.interview.service;

import com.evri.interview.exception.ValidationException;
import com.evri.interview.model.Courier;
import com.evri.interview.repository.CourierRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CourierService {

    private CourierTransformer courierTransformer;
    private CourierRepository repository;

    public List<Courier> getAllCouriers(boolean isActive) {
        return repository.findAll()
                .stream()
                .filter(entity -> isActive == entity.isActive())
                .map(courierTransformer::toCourier)
                .toList();
    }

    public void update(long courierId, Courier data) {
        repository.findById(courierId).ifPresentOrElse(courierEntity ->
                    repository.save(courierTransformer.toCourierEntity(courierEntity.getId(), data)),
                () -> {
                    throw new ValidationException(String.format("Courier with id %s is not found", courierId));
                }
        );
    }
}
