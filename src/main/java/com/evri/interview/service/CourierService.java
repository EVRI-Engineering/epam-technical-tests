package com.evri.interview.service;

import com.evri.interview.model.Courier;
import com.evri.interview.repository.CourierEntity;
import com.evri.interview.repository.CourierRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class CourierService {

    private CourierTransformer courierTransformer;
    private CourierRepository repository;

    public List<Courier> getAllCouriers(boolean isActive) {
        Stream<CourierEntity> courierEntities = repository.findAll().stream();
        if (isActive) {
            courierEntities = courierEntities.filter(CourierEntity::isActive);
        }
        return courierEntities
                .map(courierTransformer::toCourier)
                .collect(Collectors.toList());
    }

    public Optional<CourierEntity> findById(Long id) {
        return repository.findById(id);
    }

    public Courier update(Courier courier) {
        CourierEntity courierEntity = courierTransformer.toCourierEntity(courier);

        CourierEntity result = repository.save(courierEntity);
        return courierTransformer.toCourier(result);
    }
}
