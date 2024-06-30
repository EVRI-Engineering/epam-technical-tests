package com.evri.interview.service;

import com.evri.interview.exception.CourierNotFoundException;
import com.evri.interview.model.Courier;
import com.evri.interview.entity.CourierEntity;
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

    public List<Courier> getCouriers(boolean fetchActive) {
        return fetchActive ? getAllActiveCouriers() : getAllCouriers();
    }

    private List<Courier> getAllCouriers() {
        return repository.findAll()
                .stream()
                .map(courierTransformer::toCourier)
                .collect(Collectors.toList());
    }

    private List<Courier> getAllActiveCouriers() {
        return repository.findByActiveTrue()
                .stream()
                .map(courierTransformer::toCourier)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateCourierDetails(Courier courier, Long courierId) {
        CourierEntity courierEntity = repository.findById(courierId)
                .orElseThrow(() -> getCourierNotFoundException(courierId));

        CourierEntity updatedEntity = courierTransformer.toUpdatedCourierEntity(courierEntity, courier);
        repository.save(updatedEntity);
    }

    private CourierNotFoundException getCourierNotFoundException(Long courierId) {
        return new CourierNotFoundException(String.format("Courier with id %s was not found", courierId));
    }
}
