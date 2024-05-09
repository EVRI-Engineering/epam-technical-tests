package com.evri.interview.service;

import com.evri.interview.model.Courier;
import com.evri.interview.model.SaveCourier;
import com.evri.interview.repository.CourierEntity;
import com.evri.interview.repository.CourierRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

@Service
@AllArgsConstructor
public class CourierService {

    private CourierTransformer courierTransformer;
    private CourierRepository repository;

    public List<Courier> getAllCouriers(boolean isActive) {
        if (isActive) {
            return repository.findAll()
                    .stream()
                    .filter(CourierEntity::isActive)
                    .map(courierTransformer::toCourier)
                    .collect(Collectors.toList());
        }

        return repository.findAll()
                .stream()
                .map(courierTransformer::toCourier)
                .collect(Collectors.toList());
    }

    public Optional<Courier> updateCourier(long courierId, SaveCourier newCourier) {
        return repository.findById(courierId)
                .map(
                        courier -> {
                            setIfPresent(courier::setFirstName, newCourier.getFirstName());
                            setIfPresent(courier::setLastName, newCourier.getLastName());
                            setIfPresent(courier::setActive, newCourier.isActive());
                            repository.save(courier);

                            return Optional.of(
                                    courierTransformer.toCourier(courier));
                        }
        ).orElseGet(Optional::empty);
    }

    private <T> void setIfPresent(Consumer<T> consumer, T value) {
        if (nonNull(value)) {
            consumer.accept(value);
        }
    }
}
