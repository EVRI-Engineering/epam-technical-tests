package com.evri.interview.service;

import com.evri.interview.dto.CourierRequest;
import com.evri.interview.dto.CourierResponse;
import com.evri.interview.exception.ResourceNotFoundException;
import com.evri.interview.mapper.CourierTransformer;
import com.evri.interview.model.CourierEntity;
import com.evri.interview.repository.CourierRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class CourierService implements ICourierService {

    private final CourierTransformer courierTransformer;
    private final CourierRepository repository;

    @Transactional(readOnly = true)
    @Override
    public List<CourierResponse> getAllCouriers(boolean isActive) {
        List<CourierEntity> couriers;
        if (isActive) {
            log.info("Attempt to get couriers with active flag is true.");
            couriers = repository.findByActive(true);
        } else {
            log.info("Attempt to get all couriers.");
            couriers = repository.findAll();
        }
        return couriers
                .stream()
                .map(courierTransformer::toCourier)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public CourierResponse updateCourier(long courierId, CourierRequest courierRequest) {
        log.info("Attempt to update courier with id={}.", courierId);
        checkIfUserExists(courierId);
        final CourierEntity userToUpdate = courierTransformer.toCourierEntity(courierId, courierRequest);
        CourierEntity updatedCourier = repository.save(userToUpdate);
        log.info("Courier was updated.");
        return courierTransformer.toCourier(updatedCourier);
    }

    private void checkIfUserExists(long courierId) {
        if (!repository.existsById(courierId)) {
            throw new ResourceNotFoundException(String.format("Courier not found. courierId = %d", courierId));
        }
    }

}
