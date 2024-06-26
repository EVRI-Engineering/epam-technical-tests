package com.evri.interview.service;

import com.evri.interview.exception.CourierNotFoundException;
import com.evri.interview.model.Courier;
import com.evri.interview.repository.CourierEntity;
import com.evri.interview.repository.CourierRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public List<Courier> getActiveCouriers() {
        return repository.findAllByActive(true)
            .stream()
            .map(courierTransformer::toCourier)
            .collect(Collectors.toList());
    }

    @Transactional
    public Courier updateCourierById(long id, Courier updatedCourierDetails) {
        return repository.findById(id)
            .map(courierEntity -> {
                courierEntity.setFirstName(updatedCourierDetails.getFirstName());
                courierEntity.setLastName(updatedCourierDetails.getLastName());
                courierEntity.setActive(updatedCourierDetails.isActive());
                CourierEntity updatedCourierEntity = repository.save(courierEntity);
                return courierTransformer.toCourier(updatedCourierEntity);
            })
            .orElseThrow(() -> new CourierNotFoundException("Courier not found with ID: " + id));
    }
}
