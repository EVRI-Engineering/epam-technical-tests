package com.evri.interview.service;

import com.evri.interview.exception.RecordNotFoundException;
import com.evri.interview.dto.CourierRequestDto;
import com.evri.interview.dto.CourierResponseDto;
import com.evri.interview.mapper.CourierTransformer;
import com.evri.interview.model.CourierEntity;
import com.evri.interview.repository.CourierRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CourierService {

    private CourierTransformer courierTransformer;
    private CourierRepository repository;

    public List<CourierResponseDto> getAllCouriers(boolean isActive) {
        List<CourierEntity> courierEntities = isActive ? repository.findByActiveTrue() : repository.findAll();

        return courierEntities
                .stream()
                .map(courierTransformer::toCourier)
                .collect(Collectors.toList());
    }


    public CourierResponseDto updateCourierById(long id, CourierRequestDto courierRequestDto) {
        if (!repository.existsById(id)) {
            throw new RecordNotFoundException(String.format("Courier with id [%d] is not found", id));
        }

        courierRequestDto.setId(id);
        CourierEntity courierEntity = courierTransformer.toCourierEntity(courierRequestDto);
        CourierEntity updatedEntity = repository.save(courierEntity);
        return courierTransformer.toCourier(updatedEntity);
    }
}
