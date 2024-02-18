package com.evri.interview.service;

import com.evri.interview.exception.CourierNotFoundException;
import com.evri.interview.model.Courier;
import com.evri.interview.repository.CourierEntity;
import com.evri.interview.repository.CourierRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CourierService {

    private CourierTransformer courierTransformer;
    private CourierRepository repository;

    public List<Courier> getAllCouriers(boolean isActive) {
        return repository.findAll()
                .stream()
                .filter(isActive ? CourierEntity::isActive : courierEntity -> true)
                .map(courierTransformer::toCourier)
                .collect(Collectors.toList());
    }

    public void updateCourierById(Long courierId, Courier courier) {
        Optional<CourierEntity> existedCourier = repository.findById(courierId);
        if (!existedCourier.isPresent()) {
            throw new CourierNotFoundException(String.format("Courier not found by id: %d", courierId));
        }

        repository.save(courierTransformer.toCourierEntity(courier, courierId));
    }
}
