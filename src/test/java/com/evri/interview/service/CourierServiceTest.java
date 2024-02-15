package com.evri.interview.service;

import com.evri.interview.dataprovider.DataProvider;
import com.evri.interview.exception.ResourceNotFoundException;
import com.evri.interview.model.Courier;
import com.evri.interview.repository.CourierEntity;
import com.evri.interview.repository.CourierRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CourierServiceTest {

    @InjectMocks
    private CourierService courierService;

    @Mock
    private CourierTransformer courierTransformer;

    @Mock
    private CourierRepository repository;

    @Test
    @DisplayName("Get all couriers should get")
    void getAllCouriers_shouldGetTest() {
        List<Courier> expectedCouriers = DataProvider.getCouriers();
        List<CourierEntity> courierEntities = DataProvider.getCourierEntities();
        given(repository.findAll()).willReturn(courierEntities);
        given(courierTransformer.toCourier(courierEntities.get(0))).willReturn(expectedCouriers.get(0));
        given(courierTransformer.toCourier(courierEntities.get(1))).willReturn(expectedCouriers.get(1));
        List<Courier> allCouriers = courierService.getAllCouriers(false);
        assertThat(expectedCouriers).isEqualTo(allCouriers);
        verify(repository).findAll();
        verify(courierTransformer).toCourier(courierEntities.get(0));
    }

    @Test
    @DisplayName("Get active couriers should get")
    void getActiveCouriers_shouldGetTest() {
        List<Courier> expectedCouriers = DataProvider.getCouriers();
        List<CourierEntity> courierEntities = DataProvider.getCourierEntities();
        given(repository.findAllByActive(true)).willReturn(courierEntities);
        given(courierTransformer.toCourier(courierEntities.get(0))).willReturn(expectedCouriers.get(0));
        given(courierTransformer.toCourier(courierEntities.get(1))).willReturn(expectedCouriers.get(1));
        List<Courier> allCouriers = courierService.getAllCouriers(true);
        assertThat(expectedCouriers).isEqualTo(allCouriers);
        verify(repository).findAllByActive(true);
        verify(courierTransformer).toCourier(courierEntities.get(0));
        verify(courierTransformer).toCourier(courierEntities.get(1));
    }

    @Test
    @DisplayName("Update courier should update test")
    void updateCourier_shouldUpdateTest() {
        long courierId = 1;
        CourierEntity courierEntity = DataProvider.getCourierEntity(courierId, "Ben", "Askew");
        Courier courierUpdateRequest = DataProvider.getCourier(courierId, "Updated Name");
        given(repository.findById(courierId))
                .willReturn(Optional.of(courierEntity));
        given(repository.save(courierEntity)).willReturn(courierEntity);
        courierService.updateCourier(courierId, courierUpdateRequest);
        verify(repository).findById(courierId);
        verify(repository).save(courierEntity);
    }

    @Test

    @DisplayName("Update courier that does not exist should throw exception")
    void updateCourierThatDoesNotExist_shouldThrowExceptionTest() {
        long courierId = 1;
        Courier courierUpdateRequest = DataProvider.getCourier(courierId, "Updated Name");
        given(repository.findById(courierId))
                .willReturn(Optional.empty());
        assertThatThrownBy(() -> courierService.updateCourier(courierId, courierUpdateRequest))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Courier does not exists");
        verify(repository).findById(courierId);
    }
}
