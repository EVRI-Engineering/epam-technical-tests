package com.evri.interview.service;

import com.evri.interview.model.UpdateCourierRequest;
import com.evri.interview.repository.CourierEntity;
import com.evri.interview.repository.CourierRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CourierServiceTest {

    @InjectMocks
    private CourierService subject;

    @Mock
    private CourierRepository courierRepository;

    @Mock
    private CourierTransformer courierTransformer;

    @Captor
    private ArgumentCaptor<CourierEntity> courierEntityArgumentCaptor;

    @Test
    void getAllCouriers() {
        CourierEntity courierEntity = new CourierEntity();
        List<CourierEntity> courierEntities = singletonList(courierEntity);

        when(courierRepository.findAll()).thenReturn(courierEntities);

        subject.getAllCouriers();

        verify(courierRepository).findAll();
        verify(courierTransformer).toCouriers(courierEntities);
    }

    @Test
    void getAllActiveCouriers() {
        CourierEntity courierEntity = new CourierEntity();
        List<CourierEntity> courierEntities = singletonList(courierEntity);

        when(courierRepository.findAllByActiveTrue()).thenReturn(courierEntities);

        subject.getAllActiveCouriers();

        verify(courierRepository).findAllByActiveTrue();
        verify(courierTransformer).toCouriers(courierEntities);
    }

    @Test
    void updateCourier() {
        long courierId = 1;
        boolean isActive = true;
        String firstName = "firstName";
        String lastName = "lastName";
        UpdateCourierRequest updateCourierRequest = createUpdateCourierRequest(isActive, firstName, lastName);
        CourierEntity expected = new CourierEntity(courierId, firstName, lastName, isActive);

        when(courierRepository.existsById(courierId)).thenReturn(true);
        when(courierRepository.save(expected)).thenReturn(expected);

        subject.updateCourier(courierId, updateCourierRequest);

        verify(courierRepository).save(courierEntityArgumentCaptor.capture());
        CourierEntity actual = courierEntityArgumentCaptor.getValue();
        assertThat(actual).isEqualTo(expected);
        verify(courierTransformer).toCourier(expected);
    }

    @Test
    void updateCourier_courierDoesNotExist() {
        long courierId = 1;
        boolean isActive = true;
        String firstName = "firstName";
        String lastName = "lastName";
        UpdateCourierRequest updateCourierRequest = createUpdateCourierRequest(isActive, firstName, lastName);

        when(courierRepository.existsById(courierId)).thenReturn(false);

        assertThatThrownBy(() -> subject.updateCourier(courierId, updateCourierRequest))
                .isInstanceOf(EntityNotFoundException.class);
    }

    private UpdateCourierRequest createUpdateCourierRequest(boolean isActive, String firstName, String lastName) {
        UpdateCourierRequest updateCourierRequest = new UpdateCourierRequest();

        updateCourierRequest.setActive(isActive);
        updateCourierRequest.setFirstName(firstName);
        updateCourierRequest.setLastName(lastName);

        return updateCourierRequest;
    }
}
