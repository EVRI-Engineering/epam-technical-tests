package com.evri.interview.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.evri.interview.model.Courier;
import com.evri.interview.model.CourierUpdateDto;
import com.evri.interview.repository.CourierRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CourierService {

    private CourierTransformer courierTransformer;
    private CourierRepository repository;

    public List<Courier> getAllActiveCouriers() {
        return repository.findAllByActiveTrue()
          .stream()
          .map(courierTransformer::toCourier)
          .toList();
    }

    public List<Courier> getAllCouriers() {
        return repository.findAll()
                .stream()
                .map(courierTransformer::toCourier)
          .toList();
    }

    public Optional<Courier> getCourierByID(final long id) {
        return repository.findById(id).map(entity -> courierTransformer.toCourier(entity));
    }

    public Courier saveCourier(final Courier courier) {
        return courierTransformer.toCourier(repository.save(courierTransformer.toCourierEntity(courier)));
    }

    @Transactional
    public Optional<Courier> updateCourier(long courierId, CourierUpdateDto courierDto) {
        final Optional<Courier> courier = getCourierByID(courierId);
        if (courier.isEmpty()) {
            return Optional.empty();
        }
        Courier updatedCourier = saveCourier(courierTransformer.fromDto(courierId, courierDto));
        return Optional.of(updatedCourier);
    }
}
