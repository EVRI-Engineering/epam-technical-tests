package com.evri.interview.service;

import com.evri.interview.model.Courier;
import com.evri.interview.repository.CourierEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.Stream;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CourierTransformerTest {

    @Spy
    private CourierTransformer subject;

    @Test
    void toCourier() {
        long courierId = 1;
        boolean isActive = true;
        String firstName = "firstName";
        String lastName = "lastName";
        CourierEntity courierEntity = new CourierEntity(courierId, firstName, lastName, isActive);
        Courier expected = Courier.builder()
                .id(courierId)
                .name(format("%s %s", firstName, lastName))
                .active(isActive)
                .build();

        Courier actual = subject.toCourier(courierEntity);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void toCouriers() {
        List<CourierEntity> courierEntities = Stream.iterate(0, x -> x + 1)
                .map(x -> new CourierEntity(x, format("firstName%d", x), format("lastName%d", x), true))
                .limit(5)
                .collect(toList());

        subject.toCouriers(courierEntities);

        verify(subject, times(courierEntities.size())).toCourier(any(CourierEntity.class));
    }
}
