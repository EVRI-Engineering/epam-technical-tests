package com.evri.interview.service;

import com.evri.interview.model.Courier;
import com.evri.interview.model.UpdateCourier;
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
        List<CourierEntity> courierEntities = isActive ? repository.findByActiveTrue() : repository.findAll();
        return courierEntities
                .stream()
                .map(courierTransformer::toCourier)
                .collect(Collectors.toList());
    }

    public Optional<Courier> updateCourierById(Long id, UpdateCourier updateCourier) {
        return repository.findById(id).map(entity -> {
            entity.setFirstName(updateCourier.getFirstName());
            entity.setLastName(updateCourier.getLastName());
            entity.setActive(updateCourier.getActive());
            CourierEntity updatedEntity = repository.save(entity);
            return Optional.of(courierTransformer.toCourier(updatedEntity));
        }).orElse(Optional.empty());
    }
}
