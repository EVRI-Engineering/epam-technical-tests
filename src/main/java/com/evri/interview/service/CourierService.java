package com.evri.interview.service;

import com.evri.interview.model.Courier;
import com.evri.interview.model.CourierDTO;
import com.evri.interview.repository.CourierEntity;
import com.evri.interview.repository.CourierRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
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
        return repository.findAllByActive(true)
            .stream()
            .map(courierTransformer::toCourier)
            .collect(Collectors.toList());
    }

    @Transactional
    public Courier updateCourierById(long courierId, CourierDTO courierDTO) {
        return repository.findById(courierId)
            .map(courierEntity -> {
                courierEntity.setId(courierEntity.getId());
                courierEntity.setFirstName(Objects.isNull(courierDTO.getFirstName()) ? courierEntity.getFirstName() : courierDTO.getFirstName());
                courierEntity.setLastName(Objects.isNull(courierDTO.getLastName()) ? courierEntity.getLastName() : courierDTO.getLastName());
                courierEntity.setActive(courierDTO.isActive());
                CourierEntity updatedEntity = repository.save(courierEntity);
                return courierTransformer.toCourier(updatedEntity);
            }).orElseThrow(() -> new IllegalArgumentException("Courier with id " + courierId + " does not exist"));
    }
}
