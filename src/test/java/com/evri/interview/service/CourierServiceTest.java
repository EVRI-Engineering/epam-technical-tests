package com.evri.interview.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.evri.interview.exception.CourierNotFoundException;
import com.evri.interview.model.Courier;
import com.evri.interview.repository.CourierEntity;
import com.evri.interview.repository.CourierRepository;

@ExtendWith(MockitoExtension.class)
public class CourierServiceTest {

    @Mock
    private CourierRepository courierRepository;

    @Mock
    private CourierTransformer courierTransformer;

    private CourierService courierService;

    @BeforeEach
    public void setup() {
        courierService = new CourierService(courierTransformer, courierRepository);
    }

    @Test
    public void shouldGetActiveCouriers() {
        CourierEntity courierEntity = CourierEntity.builder().id(1L).firstName("James").lastName("Brown").active(true).build();
        Courier courier = Courier.builder().id(1L).firstName("James").lastName("Brown").active(true).build();

        when(courierRepository.findAllByActive(true)).thenReturn(Arrays.asList(courierEntity));
        when(courierTransformer.toCourier(courierEntity)).thenReturn(courier);

        List<Courier> couriers = courierService.getActiveCouriers();

        assertThat(couriers.size(), equalTo(1));
        assertThat(couriers.get(0).getId(), equalTo(1L));
        assertThat(couriers.get(0).getFirstName(), equalTo("James"));
        assertThat(couriers.get(0).getLastName(), equalTo("Brown"));
        assertThat(couriers.get(0).isActive(), equalTo(true));
    }

    @Test
    public void shouldUpdateCourierById() {
        long id = 1L;
        CourierEntity existingCourierEntity = CourierEntity.builder().id(id).firstName("James").lastName("Brown").active(true).build();
        Courier updatedCourierDetails = Courier.builder().id(id).firstName("James").lastName("Brown").active(true).build();
        CourierEntity updatedCourierEntity = CourierEntity.builder().id(id).firstName("James").lastName("Brown").active(true).build();

        when(courierRepository.findById(id)).thenReturn(Optional.of(existingCourierEntity));
        when(courierRepository.save(existingCourierEntity)).thenReturn(updatedCourierEntity);
        when(courierTransformer.toCourier(updatedCourierEntity)).thenReturn(updatedCourierDetails);

        Courier updatedCourier = courierService.updateCourierById(id, updatedCourierDetails);

        assertThat(updatedCourier, notNullValue());
        assertThat(updatedCourier.getId(), equalTo(id));
        assertThat(updatedCourier.getFirstName(), equalTo("James"));
        assertThat(updatedCourier.getLastName(), equalTo("Brown"));
        assertThat(updatedCourier.isActive(), equalTo(true));
    }

    @Test
    public void shouldThrowExceptionWhenCourierNotFound() {
        long id = 100L;
        Courier updatedCourierDetails = Courier.builder().id(id).firstName("James").lastName("Brown").active(true).build();
        when(courierRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(CourierNotFoundException.class, () -> courierService.updateCourierById(id, updatedCourierDetails));
    }
}
