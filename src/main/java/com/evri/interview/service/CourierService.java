package com.evri.interview.service;

import com.evri.interview.dto.CourierDto;
import com.evri.interview.exception.UpdateNonExistantCourierException;
import com.evri.interview.mapper.CourierTransformer;
import com.evri.interview.model.Courier;
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
public class CourierService {

    private CourierTransformer courierTransformer;
    private CourierRepository repository;

    public List<Courier> getAllActiveCouriers(Boolean isActive) {
        return repository.findAll()
                .stream()
                .filter(entity -> isActive == entity.isActive())
                .map(courierTransformer::toCourier)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateCourier(long id, CourierDto dto) {
        repository.findById(id).ifPresentOrElse(courierEntity ->
                        repository.save(courierTransformer.toCourierEntity(courierEntity.getId(), dto)),
                () -> {
                    throw new UpdateNonExistantCourierException(String.format("Attempt to update non existent courier with id %s, new courier has bean created", id));
                }
        );
    }
}