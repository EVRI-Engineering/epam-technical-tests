package com.evri.interview.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.evri.interview.model.Courier;
import com.evri.interview.model.CourierPutDataModel;
import com.evri.interview.repository.CourierEntity;
import com.evri.interview.repository.CourierRepository;
import com.evri.interview.exception.CourierNotFoundException;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CourierService {

    private CourierTransformer courierTransformer;
    private CourierRepository repository;

    @Transactional(readOnly = true)
    public List<Courier> getAllCouriers(final boolean isActive) {

        if (isActive) {
            return repository.findAllByActiveTrue().stream()
                .map(courierTransformer::toCourier)
                .collect(Collectors.toList());
        }

        return repository.findAll()
            .stream()
            .map(courierTransformer::toCourier)
            .collect(Collectors.toList());
    }

    @Transactional
    public void updateCourierData(final Long courierId, final CourierPutDataModel courierPutDataModel) {

        if (!repository.existsById(courierId)) {
            throw new CourierNotFoundException(courierId);
        }

        final CourierEntity courier = courierTransformer.toEntity(courierId, courierPutDataModel);
        repository.save(courier);
    }
}
