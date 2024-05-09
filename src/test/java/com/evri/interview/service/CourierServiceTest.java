package com.evri.interview.service;

import com.evri.interview.model.Courier;
import com.evri.interview.model.SaveCourier;
import com.evri.interview.repository.CourierEntity;
import com.evri.interview.repository.CourierRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CourierServiceTest {

    private final Integer ID = 1;
    private final String FIRST_NANE = "Unknown";
    private final String LAST_NANE = "Unknown";

    @Spy
    private CourierTransformer courierTransformer = new CourierTransformer();

    @Mock
    private CourierRepository repository;

    @InjectMocks
    private CourierService service;

    @Test
    void shouldReturnNoResultIfCourierNotFound() {
        // Given
        Optional<CourierEntity> courier = Optional.empty();

        // When
        when(repository.findById(anyLong())).thenReturn(courier);

        // Then
        Optional<Courier> actualCourier = service.updateCourier(
                ID, SaveCourier.builder()
                        .firstName(FIRST_NANE)
                        .lastName(LAST_NANE)
                        .active(true)
                        .build()
        );

        assertThat(actualCourier).isEmpty();

    }

    @Test
    void shouldReturnUpdatedCourierIfTheCourierWasFound() {
        // Given
        Optional<CourierEntity> courier = Optional.of(
                CourierEntity.builder()
                        .id(ID)
                        .firstName(FIRST_NANE)
                        .lastName(LAST_NANE)
                        .active(true)
                        .build()
        );

        // When
        when(repository.findById(anyLong())).thenReturn(courier);

        Optional<Courier> expectedCourier = Optional.of(
                Courier.builder()
                        .id(ID)
                        .name(format("%s %s", FIRST_NANE, LAST_NANE + "_1"))
                        .active(false)
                        .build()
        );

        // Then
        Optional<Courier> actualCourier = service.updateCourier(
                ID, SaveCourier.builder()
                        .lastName(LAST_NANE + "_1")
                        .active(false)
                        .build()
        );

        assertThat(actualCourier).isNotEmpty();
        assertThat(actualCourier).isEqualTo(expectedCourier);

    }

    @Test
    void shouldSuccessfullyReturnAllCouriersIfIsActiveFalse() {
        // Given
        final boolean isActive = false;
        List<CourierEntity> couriers = Arrays.asList(
                CourierEntity.builder()
                        .id(ID)
                        .firstName(FIRST_NANE)
                        .lastName(LAST_NANE)
                        .active(true)
                        .build(),
                CourierEntity.builder()
                        .id(ID + 1)
                        .firstName(FIRST_NANE)
                        .lastName(LAST_NANE)
                        .active(false)
                        .build()
        );

        // When
        when(repository.findAll()).thenReturn(couriers);

        List<Courier> expectedCouriers = Arrays.asList(
                Courier.builder()
                        .id(ID)
                        .name(format("%s %s", FIRST_NANE, LAST_NANE))
                        .active(true)
                        .build(),
                Courier.builder()
                        .id(ID + 1)
                        .name(format("%s %s", FIRST_NANE, LAST_NANE))
                        .active(false)
                        .build()
        );

        // Then
        List<Courier> actualCourier = service.getAllCouriers(isActive);

        assertThat(actualCourier).hasSize(2);
        assertThat(actualCourier).isEqualTo(expectedCouriers);

    }

    @Test
    void shouldSuccessfullyReturnOnlyActiveCouriersIfIsActiveTrue() {
        // Given
        final boolean isActive = true;
        List<CourierEntity> couriers = Arrays.asList(
                CourierEntity.builder()
                        .id(ID)
                        .firstName(FIRST_NANE)
                        .lastName(LAST_NANE)
                        .active(true)
                        .build(),
                CourierEntity.builder()
                        .id(ID + 1)
                        .firstName(FIRST_NANE)
                        .lastName(LAST_NANE)
                        .active(false)
                        .build()
        );

        // When
        when(repository.findAll()).thenReturn(couriers);

        List<Courier> expectedCouriers = Arrays.asList(
                Courier.builder()
                        .id(ID)
                        .name(format("%s %s", FIRST_NANE, LAST_NANE))
                        .active(true)
                        .build()
        );

        // Then
        List<Courier> actualCourier = service.getAllCouriers(isActive);

        assertThat(actualCourier).hasSize(1);
        assertThat(actualCourier).isEqualTo(expectedCouriers);

    }

}
