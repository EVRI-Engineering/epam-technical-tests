package com.evri.interview.controller;

import com.evri.interview.model.Courier;
import com.evri.interview.model.UpdateCourierRequest;
import com.evri.interview.service.CourierService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static java.lang.String.format;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CourierControllerTest {

    @InjectMocks
    private CourierController subject;

    @Mock
    private CourierService courierService;

    @Test
    void getAllCouriers_isActiveIsFalse() {
        Courier courier = Courier.builder().build();
        List<Courier> couriers = singletonList(courier);

        when(courierService.getAllCouriers()).thenReturn(couriers);

        ResponseEntity<List<Courier>> actual = subject.getAllCouriers(false);

        verify(courierService).getAllCouriers();
        verifyNoMoreInteractions(courierService);
        assertThat(actual).isEqualTo(ResponseEntity.ok(couriers));
    }

    @Test
    void getAllCouriers_IsActiveIsTrue() {
        Courier courier = Courier.builder().build();
        List<Courier> couriers = singletonList(courier);

        when(courierService.getAllActiveCouriers()).thenReturn(couriers);

        ResponseEntity<List<Courier>> actual = subject.getAllCouriers(true);

        verify(courierService).getAllActiveCouriers();
        verifyNoMoreInteractions(courierService);
        assertThat(actual).isEqualTo(ResponseEntity.ok(couriers));
    }

    @Test
    void updateCourier() {
        long courierId = 1;
        boolean isActive = true;
        String firstName = "firstName";
        String lastName = "lastName";
        UpdateCourierRequest updateCourierRequest = new UpdateCourierRequest();
        updateCourierRequest.setActive(isActive);
        updateCourierRequest.setFirstName(firstName);
        updateCourierRequest.setLastName(lastName);
        Courier courier = Courier.builder()
                .id(courierId)
                .name(format("%s %s", firstName, lastName))
                .active(isActive)
                .build();

        ResponseEntity<Courier> expected = ResponseEntity.ok(courier);

        when(courierService.updateCourier(courierId, updateCourierRequest)).thenReturn(courier);

        ResponseEntity<Courier> actual = subject.updateCourier(courierId, updateCourierRequest);

        verify(courierService).updateCourier(courierId, updateCourierRequest);
        verifyNoMoreInteractions(courierService);
        assertThat(actual).isEqualTo(expected);
    }
}
