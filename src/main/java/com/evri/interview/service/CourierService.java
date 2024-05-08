package com.evri.interview.service;

import com.evri.interview.model.Courier;
import com.evri.interview.model.UpdateCourierRequest;
import com.evri.interview.repository.CourierEntity;
import com.evri.interview.repository.CourierRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static java.lang.String.format;

@Service
@AllArgsConstructor
public class CourierService {

    private CourierTransformer courierTransformer;
    private CourierRepository repository;

    public List<Courier> getAllCouriers() {
        return courierTransformer.toCouriers(repository.findAll());
    }

    public List<Courier> getAllActiveCouriers() {
        return courierTransformer.toCouriers(repository.findAllByActiveTrue());
    }

    public Courier updateCourier(long id, UpdateCourierRequest updateCourierRequest) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException(format("Courier not found with the following id: %d", id));
        }

        return courierTransformer.toCourier(repository.save(
                CourierEntity.builder()
                        .id(id)
                        .firstName(updateCourierRequest.getFirstName())
                        .lastName(updateCourierRequest.getLastName())
                        .active(updateCourierRequest.isActive())
                        .build()
        ));
    }
}
