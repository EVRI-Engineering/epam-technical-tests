package com.evri.interview.service;

import com.evri.interview.model.Courier;
import com.evri.interview.model.CourierDTO;
import com.evri.interview.repository.CourierEntity;
import com.evri.interview.repository.CourierRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CourierService {

    private CourierTransformer courierTransformer;
    private CourierRepository repository;

    public List<Courier> getAllCouriersWithActiveStatus(boolean activeStatus) {
        return repository.findByActiveIs(activeStatus)
                .stream()
                .map(courierTransformer::toCourier)
                .collect(Collectors.toList());
    }

    public Optional<CourierDTO> updateCourier(long courierId, CourierDTO courier) {
        if (!repository.existsById(courierId)) {
            return Optional.empty();
        }

        CourierEntity entity = courierTransformer.toCourierEntity(courierId, courier);
        entity.setId(courierId);
        repository.save(entity);
        return Optional.of(courier);
    }
}
