package com.evri.interview.service;

import com.evri.interview.exception.EntityNotFoundException;
import com.evri.interview.model.Courier;
import com.evri.interview.repository.CourierEntity;
import com.evri.interview.repository.CourierRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class CourierService {

    private static final String NOT_FOUND_MESSAGE = "Failed to get courier with id %s";

    private CourierTransformer courierTransformer;
    private CourierRepository repository;

    public List<Courier> getAllCouriers(boolean isOnlyActive) {
        Stream<CourierEntity> stream = repository.findAll().stream();
        if(isOnlyActive) {
           stream = stream.filter(CourierEntity::isActive);
        }
        return stream.map(courierTransformer::toCourier)
                .collect(Collectors.toList());
    }

    public long updateCourier(long courierId, Courier data) {
        repository.findById(courierId).ifPresentOrElse(courierEntity ->
                        repository.save(courierTransformer.toCourierEntity(courierId, data)),
                () -> {
                    throw new EntityNotFoundException(String.format(NOT_FOUND_MESSAGE, courierId));
                }
        );
        return courierId;
    }
}
