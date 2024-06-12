package com.evri.interview.service;

import com.evri.interview.controller.dto.CourierDto;
import com.evri.interview.controller.exception.CourierNotFoundException;
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

    public List<Courier> getAllCouriers(boolean isActive) {
        return getCourierEntities(isActive)
                .stream()
                .map(courierTransformer::toCourier)
                .collect(Collectors.toList());
    }

    private List<CourierEntity> getCourierEntities(boolean isActive) {
        return isActive ? repository.findAll() : repository.findAllByActiveTrue();
    }

    public Courier updateCourier(Long courierId, CourierDto dto) {
        return repository.findById(courierId)
                .map(entity -> {
                    courierTransformer.merge(entity, dto);
                    return repository.save(entity);
                })
                .map(courierTransformer::toCourier)
                .orElseThrow(() -> new CourierNotFoundException("Courier with id {0} was not found", courierId));
    }
}
