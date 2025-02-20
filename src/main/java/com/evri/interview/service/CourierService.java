package com.evri.interview.service;

import com.evri.interview.exception.CourierNotFoundException;
import com.evri.interview.model.Courier;
import com.evri.interview.repository.CourierEntity;
import com.evri.interview.repository.CourierRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.evri.interview.config.ConstantsAndVariables.COURIER_WITH_ID_NOT_FOUND;

@Service
@AllArgsConstructor
public class CourierService {

    private CourierTransformer courierTransformer;
    private CourierRepository repository;

    public List<Courier> getAllCouriers() {
        return repository.findAll()
                .stream()
                .map(courierTransformer::transformToCourier)
                .collect(Collectors.toList());
    }

    public List<Courier> getCouriers(Boolean isActive) {
        return (isActive == null) ? getAllCouriers() :
                repository.getAllByActive(isActive)
                        .stream()
                        .map(courierTransformer::transformToCourier)
                        .collect(Collectors.toList());
    }


    public Courier updateCourier(Long courierId, Courier updatedCourier) {
        return repository.findById(courierId)
                .map(existingCourier -> {
                    CourierEntity updatedEntity = repository.save(courierTransformer.transformToEntity(updatedCourier));
                    return courierTransformer.transformToCourier(updatedEntity);
                })
                .orElseThrow(() -> new CourierNotFoundException(COURIER_WITH_ID_NOT_FOUND + courierId));
    }

}