package com.evri.interview.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.evri.interview.dto.CourierDto;
import com.evri.interview.model.Courier;
import com.evri.interview.repository.CourierEntity;
import com.evri.interview.repository.CourierRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CourierService {

    private CourierTransformer courierTransformer;
    private CourierRepository courierRepository;

    public List<Courier> getAllCouriers(boolean isActive) {
        List<CourierEntity> courierEntities = isActive ? courierRepository.findByActive(isActive) : courierRepository.findAll();
        return courierEntities.stream().map(courierTransformer::toCourier).collect(Collectors.toList());
    }

    @Transactional
    public Optional<Courier> updateCourier(long courierId, CourierDto courierDto) {
        return courierRepository.findById(courierId).map(courierEntity -> {
            courierEntity.setFirstName(courierDto.getFirstName());
            courierEntity.setLastName(courierDto.getLastName());
            courierEntity.setActive(courierDto.isActive());
            return courierTransformer.toCourier(courierRepository.save(courierEntity));
        });
    }

}
