package com.evri.interview.service;

import com.evri.interview.exceptions.ResourceNotFoundException;
import com.evri.interview.model.Courier;
import com.evri.interview.model.UpdateCourierData;
import com.evri.interview.repository.CourierRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
public class CourierServiceImpl implements CourierService {

    private CourierTransformer courierTransformer;
    private CourierRepository repository;

    @Override
    public List<Courier> getAllCouriers() {
        log.debug("Execute getAllCouriers");

        return repository.findAll()
                .stream()
                .map(courierTransformer::toCourier)
                .collect(Collectors.toList());
    }

    @Override
    public List<Courier> getAllCouriersByActivity(boolean isActive) {
        log.debug("Execute getAllCouriersByActivity. isActive = {}", isActive);

        return repository.findAllByActivity(isActive)
                .stream()
                .map(courierTransformer::toCourier)
                .collect(Collectors.toList());
    }

    @Override
    public Courier updateById(long id, UpdateCourierData data) {
        log.debug("Execute updateById. id = {}, Data: {}", id, data);

        return repository.findById(id).map(courier -> {
            courier.setFirstName(data.getFirstName());
            courier.setLastName(data.getLastName());
            courier.setActive(data.getIsActive());
            repository.save(courier);
            return courierTransformer.toCourier(courier);
        }).orElseThrow(() -> new ResourceNotFoundException(String.format("Courier with id '%s' not found", id)));
    }
}
