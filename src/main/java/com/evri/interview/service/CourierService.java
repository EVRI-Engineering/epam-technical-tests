package com.evri.interview.service;

import com.evri.interview.converter.CourierConverter;
import com.evri.interview.exception.CourierNotFoundException;
import com.evri.interview.model.Courier;
import com.evri.interview.model.CourierForUpdate;
import com.evri.interview.repository.CourierEntity;
import com.evri.interview.repository.CourierRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CourierService {

    private CourierConverter courierConverter;
    private CourierRepository repository;

    public List<Courier> getCouriersByActive(boolean isActive) {
        List<CourierEntity> entities = isActive ? repository.findByActiveTrue() : repository.findAll();
        return entities.stream()
                .map(courierConverter::entityToCourier)
                .sorted(Comparator.comparing(Courier::getId))
                .collect(Collectors.toList());
    }

    public void updateCourier(long id, CourierForUpdate courier) {
        CourierEntity entity = repository.findById(id)
                .orElseThrow(() -> new CourierNotFoundException("Courier with id " + id + " does not exist"));
        repository.save(courierConverter.updateEntity(entity, courier));
    }

}
