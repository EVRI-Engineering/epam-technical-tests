package com.evri.interview.service.impl;

import com.evri.interview.exception.ApiCustomException;
import com.evri.interview.model.Courier;
import com.evri.interview.model.CourierRequestDto;
import com.evri.interview.repository.entity.CourierEntity;
import com.evri.interview.repository.CourierRepository;
import com.evri.interview.service.CourierService;
import com.evri.interview.util.CourierTransformer;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CourierServiceImpl implements CourierService {

    private CourierTransformer courierTransformer;
    private CourierRepository repository;

    @Override
    public List<Courier> getAllCouriers(boolean isActive) {
        return (isActive ? repository.findByActiveTrue() : repository.findAll())
                .stream()
                .map(courierTransformer::toCourier)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Courier updateCourierById(long courierId, CourierRequestDto courierRequestDto) {
        CourierEntity courierEntity = repository.findById(courierId).orElseThrow(
                () -> new ApiCustomException(String.format("Courier with id %s is not found", courierId))
        );
        courierEntity.setFirstName(courierRequestDto.getFirstName());
        courierEntity.setLastName(courierRequestDto.getLastName());
        courierEntity.setActive(courierRequestDto.isActive());
        CourierEntity updatedCourier = repository.save(courierEntity);

        return courierTransformer.toCourier(updatedCourier);
    }

}
