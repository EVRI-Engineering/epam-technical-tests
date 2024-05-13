package com.evri.interview.service;

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

    public List<Courier> getAllCouriers(Boolean isGetOnlyActive) {
        return repository.findAll()
                .stream()
                .filter(e -> getAllOrActive(isGetOnlyActive, e))
                .map(courierTransformer::toCourier)
                .collect(Collectors.toList());
    }

    private boolean getAllOrActive(Boolean isGetOnlyActive, CourierEntity e) {
        if (isGetOnlyActive) {
            return e.isActive();
        }
        return true;
    }
}
