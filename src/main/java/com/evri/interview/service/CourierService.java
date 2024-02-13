package com.evri.interview.service;

import com.evri.interview.exception.CourierNotFoundException;
import com.evri.interview.model.Courier;
import com.evri.interview.repository.CourierEntity;
import com.evri.interview.repository.CourierRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public List<Courier> getCouriers(Boolean isActive) {
        if (isActive == null) {
            return getAllCouriers();
        }
        return repository.getAllByActive(isActive)
                .stream()
                .map(courierTransformer::toCourier)
                .collect(Collectors.toList());
    }

    public Courier updateCourier(Long courierId, Courier updatedCourier) {
        return repository.findById(courierId)
                .map(courier -> {
                    courier = courierTransformer.toCourierEntity(updatedCourier);
                    CourierEntity saved = repository.save(courier);
                    return courierTransformer.toCourier(saved);
                })
                .orElseThrow(() -> new CourierNotFoundException("Courier not found with id " + courierId));
    }


}
