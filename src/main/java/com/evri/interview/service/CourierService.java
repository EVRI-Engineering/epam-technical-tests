package com.evri.interview.service;

import com.evri.interview.exception.ResourceNotFoundException;
import com.evri.interview.model.Courier;
import com.evri.interview.repository.CourierRepository;
import com.evri.interview.request.CourierRequestBody;

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

    public List<Courier> getAllCouriers() {
        return repository.findAll()
                .stream()
                .map(courierTransformer::toCourier)
                .collect(Collectors.toList());
    }

    @Transactional
    public Courier updateCourierById(long courierId, CourierRequestBody courierRequestBody) {
        return courierTransformer.toCourier(
            repository.findById(courierId).map(courierEntit -> {
                courierEntit.setFirstName(courierRequestBody.getFirstName());
                courierEntit.setLastName(courierRequestBody.getLastName());
                courierEntit.setActive(courierRequestBody.isActive());

                return repository.save(courierEntit);
            }).orElseThrow(
                () -> new ResourceNotFoundException(String.format("Not found courier with courierId: %s", courierId))
            )
        );
    }
}
