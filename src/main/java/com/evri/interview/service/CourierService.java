package com.evri.interview.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.evri.interview.model.Courier;
import com.evri.interview.repository.CourierEntity;
import com.evri.interview.repository.CourierRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CourierService {

    private CourierTransformer courierTransformer;
    private CourierRepository repository;

    public List<Courier> getCouriers(boolean onlyActive) {
        return (onlyActive ? repository.findByActiveTrue() : repository.findAll()).stream()
                .map(courierTransformer::toCourier)
                .collect(Collectors.toList());
    }

    public Optional<Courier> updateCourier(Long courierId, String firstName, String lastName, boolean active) {
        Optional<CourierEntity> courierOpt = repository.findById(courierId);
        if (!courierOpt.isPresent()) {
            return Optional.empty();
        }

        CourierEntity entity = courierOpt.get();
        entity.setFirstName(firstName);
        entity.setLastName(lastName);
        entity.setActive(active);

        return Optional.of(repository.save(entity)).map(courierTransformer::toCourier);
    }
}
