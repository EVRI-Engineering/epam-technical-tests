package com.evri.interview.service;

import com.evri.interview.exception.EntityNotFoundException;
import com.evri.interview.model.Courier;
import com.evri.interview.model.UpdateCourierRequest;
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

    public List<Courier> getAllActiveCouriers() {
        return repository.findByActiveTrue()
            .stream()
            .map(courierTransformer::toCourier)
            .collect(Collectors.toList());
    }

    public Courier updateCourier(final Long id, final UpdateCourierRequest updateCourierRequest) {
        return repository.findById(id)
            .map(courierEntity -> {
                courierTransformer.updateCourier(courierEntity, updateCourierRequest);
                return repository.save(courierEntity);
            })
            .map(courierTransformer::toCourier)
            .orElseThrow(() -> new EntityNotFoundException(String.format("Courier with id = %s not found", id)));
    }
}
