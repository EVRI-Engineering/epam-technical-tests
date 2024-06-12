package com.evri.interview.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import com.evri.interview.dto.CourierRequest;
import com.evri.interview.dto.CourierResponse;
import com.evri.interview.exception.ResourceNotFoundException;
import com.evri.interview.mapper.CourierTransformer;
import com.evri.interview.model.CourierEntity;
import com.evri.interview.repository.CourierRepository;
import com.evri.interview.util.DataProvider;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@ExtendWith(SpringExtension.class)
class CourierServiceTest {

    private static final String FIRST_NAME = "John";
    private static final String LAST_NAME = "Nolan";
    private static final String NAME = "John Nolan";
    @Mock
    private CourierRepository repository;
    @Spy
    private CourierTransformer courierTransformer;
    private ICourierService courierService;

    @BeforeEach
    public void init() {
        courierService = new CourierService(courierTransformer, repository);
    }

    @Test
    public void getAllCouriersShouldReturnActiveCouriers() {
        //Given
        List<CourierEntity> list = Lists.list(DataProvider.buildCourierEntity(1, FIRST_NAME, LAST_NAME, Boolean.TRUE));
        List<CourierResponse> expected = Lists.list(DataProvider.buildCourierResponse(1, NAME, Boolean.TRUE));
        when(repository.findByActive(Boolean.TRUE)).thenReturn(list);
        //When
        List<CourierResponse> allCouriers = courierService.getAllCouriers(Boolean.TRUE);
        //Then
        assertThat(allCouriers).hasSameElementsAs(expected);
        verify(repository).findByActive(Boolean.TRUE);
    }

    @Test
    public void getAllCouriersShouldReturnAllCouriers() {
        //Given
        List<CourierEntity> list = Lists.list(DataProvider.buildCourierEntity(1, FIRST_NAME, LAST_NAME, Boolean.TRUE));
        List<CourierResponse> expected = Lists.list(DataProvider.buildCourierResponse(1, NAME, Boolean.TRUE));
        when(repository.findAll()).thenReturn(list);
        //When
        List<CourierResponse> allCouriers = courierService.getAllCouriers(Boolean.FALSE);
        //Then
        assertThat(allCouriers).hasSameElementsAs(expected);
        verify(repository).findAll();
    }

    @Test
    public void updateCourierShouldReturnUpdatedCourier() {
        //Given
        CourierRequest courierRequest = DataProvider.buildCourierRequest(FIRST_NAME, LAST_NAME, Boolean.TRUE);
        CourierEntity courierEntity = DataProvider.buildCourierEntity(1, FIRST_NAME, LAST_NAME, Boolean.TRUE);
        CourierResponse expected = DataProvider.buildCourierResponse(1, NAME, Boolean.TRUE);
        when(repository.save(any(CourierEntity.class))).thenReturn(courierEntity);
        when(repository.existsById(1L)).thenReturn(Boolean.TRUE);
        //When
        CourierResponse courier = courierService.updateCourier(1, courierRequest);
        //Then
        assertThat(courier).isEqualTo(expected);
        verify(repository).save(any(CourierEntity.class));
        verify(repository).existsById(1L);
    }

    @Test
    public void updateCourierWithNonExistingIdShouldReturnThrowException() {
        //Given
        CourierRequest courierRequest = DataProvider.buildCourierRequest(FIRST_NAME, LAST_NAME, Boolean.TRUE);
        when(repository.existsById(1L)).thenReturn(Boolean.FALSE);
        //When
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> courierService.updateCourier(1, courierRequest));
        //Then
        assertThat(exception.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(exception.getMessage()).isEqualTo("Courier not found. courierId = 1");
        verify(repository, never()).save(any(CourierEntity.class));
        verify(repository).existsById(1L);
    }
}