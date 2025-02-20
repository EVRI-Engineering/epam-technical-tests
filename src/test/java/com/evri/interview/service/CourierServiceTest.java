package com.evri.interview.service;

import com.evri.interview.exception.CourierNotFoundException;
import com.evri.interview.model.Courier;
import com.evri.interview.repository.CourierEntity;
import com.evri.interview.repository.CourierRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.evri.interview.config.ConstantsAndVariables.COURIER_WITH_ID_NOT_FOUND;
import static com.evri.interview.config.ConstantsAndVariables.WRONG_NAME;
import static com.evri.interview.config.CourierTestUtils.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CourierServiceTest {

    @Mock
    private CourierTransformer courierTransformer;

    @Mock
    private CourierRepository repository;

    @InjectMocks
    private CourierService courierService;

    private List<Courier> courierList;

    private List<CourierEntity> courierEntitiesList;

    private CourierTransformer transformer;

    @BeforeEach
    void setup() {
        courierList = new ArrayList<>();
        courierList.add(courier());
        courierList.add(inactiveCourier());

        courierEntitiesList = new ArrayList<>();
        courierEntitiesList.add(courierEntity());
        courierEntitiesList.add(inactiveCourierEntity());

        transformer = new CourierTransformer();
    }

    @Test
    @DisplayName("Retrieve all couriers with default status")
    void shouldReturnAllCouriersWhenIsActiveNull() {
        when(repository.findAll()).thenReturn(courierEntitiesList);

        when(courierTransformer.transformToCourier(courierEntitiesList.get(0))).thenReturn(courierList.get(0));
        when(courierTransformer.transformToCourier(courierEntitiesList.get(1))).thenReturn(courierList.get(1));

        List<Courier> actual = courierService.getCouriers(null);

        assertThat(actual.size()).isEqualTo(2);
        assertThat(actual.get(0)).isEqualTo(courierList.get(0));
        assertThat(actual.get(1)).isEqualTo(courierList.get(1));

        verify(repository).findAll();
        verifyNoMoreInteractions(repository);

        verify(courierTransformer, times(courierList.size())).transformToCourier(any());
        verifyNoMoreInteractions(courierTransformer);
    }

    @Test
    @DisplayName("Retrieve all active couriers")
    void shouldReturnActiveCouriersWhenIsActiveTrue() {
        courierEntitiesList.remove(1);
        when(repository.getAllByActive(true)).thenReturn(courierEntitiesList);
        when(courierTransformer.transformToCourier(any())).thenReturn(courierList.get(0));

        List<Courier> actual = courierService.getCouriers(true);

        assertThat(actual.size()).isEqualTo(1);
        assertThat(actual.get(0)).isEqualTo(courierList.get(0));
        verify(repository).getAllByActive(true);
        verifyNoMoreInteractions(repository);
        verify(courierTransformer).transformToCourier(any());
        verifyNoMoreInteractions(courierTransformer);
    }

    @Test
    @DisplayName("Retrieve all non active couriers")
    void shouldReturnInactiveCouriersWhenIsActiveFalse() {
        courierEntitiesList.remove(0);
        when(repository.getAllByActive(false)).thenReturn(courierEntitiesList);
        when(courierTransformer.transformToCourier(any())).thenReturn(courierList.get(1));

        List<Courier> actual = courierService.getCouriers(false);

        assertThat(actual.size()).isEqualTo(1);
        assertThat(actual.get(0)).isEqualTo(courierList.get(1));
        verify(repository).getAllByActive(false);
        verifyNoMoreInteractions(repository);
        verify(courierTransformer).transformToCourier(any());
        verifyNoMoreInteractions(courierTransformer);
    }

    @Test
    @DisplayName("Update test")
    void shouldUpdateCourier() {
        Courier courier = courier();
        Courier updated = Courier.builder()
                .id(courier.getId())
                .name(SECOND_COURIER_FIRST_NAME + " " + SECOND_COURIER_LAST_NAME)
                .active(courier.isActive())
                .build();

        CourierEntity courierEntity = courierEntity();
        CourierEntity updatedEntity = CourierEntity.builder()
                .id(courierEntity.getId())
                .firstName(SECOND_COURIER_FIRST_NAME)
                .lastName(SECOND_COURIER_LAST_NAME)
                .active(courierEntity.isActive())
                .build();

        when(repository.findById(courier.getId())).thenReturn(Optional.of(courierEntity));
        when(courierTransformer.transformToEntity(any())).thenReturn(updatedEntity);
        when(repository.save(updatedEntity)).thenReturn(updatedEntity);
        when(courierTransformer.transformToCourier(updatedEntity)).thenReturn(updated);

        Courier actual = courierService.updateCourier(courier.getId(), updated);

        verifyEqualsCouriers( actual, updated);

        verify(repository).findById(courier.getId());
        verify(courierTransformer).transformToEntity(updated);
        verify(repository).save(updatedEntity);
        verify(courierTransformer).transformToCourier(updatedEntity);
        verifyNoMoreInteractions(repository);
        verifyNoMoreInteractions(courierTransformer);
    }



    @Test
    @DisplayName("404 When no entities for update")
    void shouldThrowExceptionWhenUpdateWithNonExistingId() {

        Courier courier = courier();
        courier.setId(INVALID_ID);
        when(repository.findById(INVALID_ID))
                .thenThrow(new CourierNotFoundException(COURIER_WITH_ID_NOT_FOUND + INVALID_ID));

        assertThatThrownBy(() -> courierService.updateCourier(courier.getId(), courier))
                .isInstanceOf(CourierNotFoundException.class)
                .hasMessageContaining(COURIER_WITH_ID_NOT_FOUND + INVALID_ID);

        verify(repository).findById(INVALID_ID);
        verifyNoMoreInteractions(repository);
        verifyNoInteractions(courierTransformer);
    }



    @Test
    @DisplayName("Transform to courier")
    void testTransformToCourier() {
        Courier courier = transformer.transformToCourier(testTransformToCourierEntity());

        assertNotNull(courier);
        assertEquals(1L, courier.getId());
        assertEquals("John Doe", courier.getName());
        assertTrue(courier.isActive());
    }

    @Test
    @DisplayName("Transform to entity")
    void testTransformToEntity() {
        CourierEntity entity = transformer.transformToEntity(testTransformToEntityCourier());

        assertNotNull(entity);
        assertEquals(2L, entity.getId());
        assertEquals("Jane", entity.getFirstName());
        assertEquals("Doe", entity.getLastName());
        assertFalse(entity.isActive());
    }

    @Test
    @DisplayName("Transform to entity with invalid name")
    void testTransformToEntityWithInvalidName() {
        IllegalStateException exception = assertThrows(IllegalStateException.class, () ->
                transformer.transformToEntity(testTransformToEntityWithInvalidNameCourier())
        );
        assertEquals(WRONG_NAME, exception.getMessage());
    }

    static void verifyEqualsCouriers(Courier a,Courier b){
        assertThat(a.getId()).isEqualTo(b.getId());
        assertThat(a.getName()).isEqualTo(b.getName());
        assertThat(a.isActive()).isEqualTo(b.isActive());
    }
}