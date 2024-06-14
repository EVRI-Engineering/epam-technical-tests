package com.evri.interview.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.evri.interview.model.Courier;
import com.evri.interview.repository.CourierRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CourierService {
    private CourierTransformer courierTransformer;
    private CourierRepository courierRepository;

    public List<Courier> getCouriers(boolean onlyActive) {
        return onlyActive ? courierRepository.findByActiveTrue() : courierRepository.findAll()
                .stream()
                .map(courierTransformer::toCourier)
                .collect(Collectors.toList());
    }

    public Optional<Courier> updateCourier(Long courierId, String firstName, String lastName, boolean active) {
        Optional<Courier> courierOptional = courierRepository.findById(courierId);
        if (courierOptional.isEmpty()) {
            return Optional.empty();
        }

        Courier courier = Courier.builder()
                .id(courierId).firstName(firstName).lastName(lastName)
                .active(active)
                .build();

        return Optional.of(courierRepository.save(courier)).map(courierTransformer::toCourier);
    }
}