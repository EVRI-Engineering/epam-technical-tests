package com.evri.interview.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.evri.interview.dto.CourierRequest;
import com.evri.interview.dto.CourierResponse;
import com.evri.interview.model.CourierEntity;
import org.junit.jupiter.api.Test;

class CourierTransformerTest {

    private final CourierTransformer courierTransformer = new CourierTransformer();

    @Test
    public void toCourierShouldTransformCourierEntityToCourierResponse() {
        //Given
        CourierEntity courierEntity = CourierEntity.builder()
                .id(1)
                .firstName("John")
                .lastName("Nolan")
                .active(true)
                .build();
        //When
        CourierResponse courierResponse = courierTransformer.toCourier(courierEntity);
        //Then
        assertThat(courierResponse.getId()).isEqualTo(1);
        assertThat(courierResponse.getName()).isEqualTo("John Nolan");
        assertThat(courierResponse.isActive()).isTrue();
    }

    @Test
    public void toCourierEntityShouldTransformCourierRequestToCourierEntity() {
        //Given
        CourierRequest courierRequest = CourierRequest.builder()
                .firstName("John")
                .lastName("Nolan")
                .active(true)
                .build();
        //When
        CourierEntity courierEntity = courierTransformer.toCourierEntity(1, courierRequest);
        //Then
        assertThat(courierEntity.getId()).isEqualTo(1);
        assertThat(courierEntity.getFirstName()).isEqualTo("John");
        assertThat(courierEntity.getLastName()).isEqualTo("Nolan");
        assertThat(courierEntity.isActive()).isTrue();
    }
}