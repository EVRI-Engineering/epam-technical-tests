package com.evri.interview.service.impl;

import com.evri.interview.dto.CourierResponseDto;
import com.evri.interview.dto.CourierUpdateDto;
import com.evri.interview.exception.EntityNotFoundException;
import com.evri.interview.mapper.CourierMapper;
import com.evri.interview.model.CourierEntity;
import com.evri.interview.repository.CourierRepository;
import com.evri.interview.service.CourierService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CourierServiceImpl implements CourierService {

    private CourierMapper courierMapper;
    private CourierRepository courierRepository;

    @Transactional(readOnly = true)
    public List<CourierResponseDto> getAllCouriers(boolean isActive) {
        List<CourierEntity> entities = isActive ? courierRepository.findByActive(isActive) : courierRepository.findAll();
        return entities.stream()
                .map(courierMapper::toCourierResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public CourierResponseDto updateCourierById(long courierId, CourierUpdateDto courierUpdateDto) {
        CourierEntity courier = getById(courierId).orElseThrow(() -> new EntityNotFoundException("Courier not found with id = " + courierId));
        courierMapper.updateCourierEntity(courier, courierUpdateDto);
        CourierEntity updatedCourier = courierRepository.save(courier);
        return courierMapper.toCourierResponseDto(updatedCourier);
    }

    private Optional<CourierEntity> getById(long courierId) {
        return courierRepository.findById(courierId);
    }
}
