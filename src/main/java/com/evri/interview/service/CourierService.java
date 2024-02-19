package com.evri.interview.service;

import com.evri.interview.exception.ResourceNotFoundException;
import com.evri.interview.model.Courier;
import com.evri.interview.repository.CourierEntity;
import com.evri.interview.repository.CourierRepository;
import com.evri.interview.request.CourierRequestBody;

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
        return (isActive) ? getAllActiveCouriers() : getAllCouriers();
    }

    /**
     * This method performs a transactional update operation on the courier 
     * identified by the specified courier ID. 
     * The provided {@code courier RequestBody} contains the updated information
     * for the courier.
     * 
     * @param courierId The unique identifier of the courier.
     * @param courierRequestBody contains the updated information for the courier.
     * @return The {@link Courier} object after an update.
     */
    @Transactional
    public Courier updateCourierById(long courierId, CourierRequestBody courierRequestBody) {
        return courierTransformer.toCourier(
            repository.findById(courierId).map(courierEntit -> {
                courierEntit.setFirstName(courierRequestBody.getFirstName());
                courierEntit.setLastName(courierRequestBody.getLastName());
                courierEntit.setActive(courierRequestBody.isActive());

                return repository.save(courierEntit);
            }).orElseThrow(
                () -> new ResourceNotFoundException(String.format("Not found courier with courierId: %s", courierId))
            )
        );
    }


    private List<Courier> getAllCouriers() {
        return repository.findAll()
                .stream()
                .map(courierTransformer::toCourier)
                .collect(Collectors.toList());
    }

    private List<Courier> getAllActiveCouriers() {
        return repository.findAll()
                .stream()
                .filter(CourierEntity::isActive)
                .map(courierTransformer::toCourier)
                .collect(Collectors.toList());
    }
}
