package com.evri.interview.service;

import com.evri.interview.model.Courier;
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

    public List<Courier> getAllCouriers() {
        return repository.findAll()
                .stream()
                .map(courierTransformer::toCourier)
                .collect(Collectors.toList());
    }

    public Optional<Courier> updateCourier(Long id, Courier courier) {
        if (!isCourierExist(id)) {
            return Optional.empty();
        }

        return Optional.of(courierTransformer.toCourier(
                repository.save(courierTransformer.toCourierEntity(courier)
                )));
    }

    public boolean isCourierExist(Long id) {
        return repository.existsById(id);
    }
}
