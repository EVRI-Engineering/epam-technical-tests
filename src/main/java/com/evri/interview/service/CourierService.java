package com.evri.interview.service;

import com.evri.interview.dto.CourierDto;
import com.evri.interview.exception.CourierNotFoundException;
import com.evri.interview.model.Courier;
import com.evri.interview.repository.CourierEntity;
import com.evri.interview.repository.CourierRepository;
import javax.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CourierService {

    private CourierTransformer courierTransformer;
    private CourierRepository repository;

    public List<Courier> getAllCouriers(boolean isActive) {
        List<CourierEntity> courierEntities = isActive ? repository.findAllByActiveTrue() : repository.findAll();
        return courierEntities
                .stream()
                .map(courierTransformer::toCourier)
                .collect(Collectors.toList());
    }

    @Transactional
    public Courier updateCourier(long courierId, CourierDto courierDto) {
        CourierEntity entityToUpdate = repository.findById(courierId)
            .orElseThrow(() -> new CourierNotFoundException(String.format("Courier with id %d not found", courierId)));

        courierTransformer.updateCourierEntity(entityToUpdate, courierDto);

        return courierTransformer.toCourier(repository.save(entityToUpdate));
    }
}
