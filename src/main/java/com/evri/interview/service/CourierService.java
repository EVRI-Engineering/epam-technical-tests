package com.evri.interview.service;

import com.evri.interview.dto.CourierNameContext;
import com.evri.interview.exception.ResourceNotFoundException;
import com.evri.interview.model.Courier;
import com.evri.interview.repository.CourierEntity;
import com.evri.interview.repository.CourierRepository;
import com.evri.interview.utils.CourierUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CourierService {

    private CourierTransformer courierTransformer;

    private CourierRepository repository;

    public List<Courier> getAllCouriers(boolean isActive) {
        return getCourierEntities(isActive)
                .stream()
                .map(courierTransformer::toCourier)
                .collect(Collectors.toList());
    }

    private List<CourierEntity> getCourierEntities(boolean isActive) {
        return isActive
                ? repository.findAllByActive(true)
                : repository.findAll();
    }

    @Transactional
    public void updateCourier(final long courierId, final Courier courier) {
        final CourierEntity courierEntity = repository.findById(courierId)
                .orElseThrow(() -> new ResourceNotFoundException("Courier does not exists"));
        updateCourierEntity(courier, courierEntity);
        repository.save(courierEntity);
    }

    private static void updateCourierEntity(final Courier courier,
                                            final CourierEntity courierEntity) {
        courierEntity.setActive(courier.isActive());
        final CourierNameContext nameContext = CourierUtils.getNameContext(courier.getName());
        courierEntity.setFirstName(nameContext.getFirstName());
        courierEntity.setLastName(nameContext.getLastName());
    }
}
