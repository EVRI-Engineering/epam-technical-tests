package com.evri.interview.service;

import com.evri.interview.exception.EntityNotFoundException;
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

    @Transactional(readOnly = true)
    public List<Courier> getCouriers(boolean isActive) {
        return (isActive ? repository.findByActiveTrue() : repository.findAll())
                .stream()
                .map(courierTransformer::toCourier)
                .collect(Collectors.toList());
    }

    @Transactional
    public Courier updateCourier(Long id, Courier courier) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException(String.format("Courier with id: %d not found", courier.getId()));
        }

        CourierEntity courierEntity = courierTransformer.toCourierEntity(id, courier);
        repository.save(courierEntity);

        return courierTransformer.toCourier(courierEntity);
    }
}
