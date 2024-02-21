package com.evri.interview.service.impl;

import com.evri.interview.exception.EntityNotFoundException;
import com.evri.interview.model.Courier;
import com.evri.interview.repository.CourierEntity;
import com.evri.interview.repository.CourierRepository;
import com.evri.interview.service.CourierService;
import com.evri.interview.service.CourierTransformer;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
@AllArgsConstructor
public class CourierServiceImpl implements CourierService {

    private CourierTransformer courierTransformer;
    private CourierRepository repository;

    @Override
    public List<Courier> getAllCouriers() {
        return repository.findAll()
                .stream()
                .map(courierTransformer::toCourier)
                .collect(Collectors.toList());
    }

    @Override
    public List<Courier> getActiveCouriers() {
        return repository.findAllByIsActive(true)
                .stream()
                .map(courierTransformer::toCourier)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public Courier updateCourierById(Courier courierDto, Long id) {
        return repository.findById(id).map(existingCourier -> {
            CourierEntity courierEntity = courierTransformer.toCourierEntity(courierDto);
            existingCourier.setActive(courierEntity.isActive());
            existingCourier.setFirstName(courierEntity.getFirstName());
            existingCourier.setLastName(courierEntity.getLastName());
            repository.save(existingCourier);
            return courierTransformer.toCourier(existingCourier);
        }).orElseThrow(() -> new EntityNotFoundException(CourierEntity.class.getSimpleName(), id));
    }
}
