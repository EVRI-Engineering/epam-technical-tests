package com.evri.interview.service;

import com.evri.interview.exception.CourierNotFoundException;
import com.evri.interview.model.Courier;
import com.evri.interview.repository.CourierRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class CourierService {

    private CourierTransformer courierTransformer;
    private CourierRepository repository;

    public List<Courier> getAllCouriersOrActive(Boolean isActive) {
        return courierTransformer.toCourierList(repository.findByActive(isActive));
    }

    public void updateCourierById(Courier courier, long courierId) {

        repository
            .findById(courierId)
            .ifPresentOrElse(
                existingCourier -> {
                    repository.save(courierTransformer.toCourierEntity(courier, courierId));
                },
                () -> {
                    throw new CourierNotFoundException(courierId);
                });
    }
}
