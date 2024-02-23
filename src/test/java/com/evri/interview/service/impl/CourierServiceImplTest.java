package com.evri.interview.service.impl;

import com.evri.interview.exception.ApiCustomException;
import com.evri.interview.model.Courier;
import com.evri.interview.model.CourierRequestDto;
import com.evri.interview.repository.entity.CourierEntity;
import com.evri.interview.repository.CourierRepository;
import com.evri.interview.util.CourierTransformer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class CourierServiceImplTest {

    @Mock
    private CourierRepository courierRepository;

    @InjectMocks
    private CourierServiceImpl courierService;

    @Mock
    private CourierTransformer courierTransformer = new CourierTransformer();

    @Test
    void test_shouldReturnAllCouriers_withActiveIsTrue() {
        List<CourierEntity> courierEntityList = getCourierEntityList().stream()
                .filter(CourierEntity::isActive)
                .collect(Collectors.toList());
        List<Courier> expectedCourierList = courierEntityList.stream()
                .map(new CourierTransformer()::toCourier)
                .collect(Collectors.toList());

        when(courierRepository.findByActiveTrue()).thenReturn(courierEntityList);
        when(courierTransformer.toCourier(any())).thenCallRealMethod();

        List<Courier> actualCouriersList = courierService.getAllCouriers(true);
        assertEquals(expectedCourierList, actualCouriersList);
        verify(courierRepository, times(1)).findByActiveTrue();
        verify(courierRepository, times(0)).findAll();
        verify(courierTransformer, times(expectedCourierList.size())).toCourier(any());
    }

    @Test
    void test_shouldReturnAllCouriers_whenActiveIsFalse() {
        List<CourierEntity> courierEntityList = getCourierEntityList();
        List<Courier> expectedCourierList = courierEntityList.stream()
                .map(new CourierTransformer()::toCourier)
                .collect(Collectors.toList());

        when(courierRepository.findAll()).thenReturn(courierEntityList);
        when(courierTransformer.toCourier(any())).thenCallRealMethod();

        List<Courier> actualCouriersList = courierService.getAllCouriers(false);
        assertEquals(expectedCourierList, actualCouriersList);
        verify(courierRepository, times(1)).findAll();
        verify(courierRepository, times(0)).findByActiveTrue();
        verify(courierTransformer, times(expectedCourierList.size())).toCourier(any());
    }

    @Test
    void test_shouldWorkOk_whenNoCouriersFound() {
        List<CourierEntity> courierEntityList = Collections.emptyList();
        when(courierRepository.findAll()).thenReturn(courierEntityList);
        List<Courier> expectedCourierList = new ArrayList<>();
        when(courierTransformer.toCourier(any())).thenCallRealMethod();

        List<Courier> actualCouriersList = courierService.getAllCouriers(false);
        assertEquals(expectedCourierList, actualCouriersList);
        verify(courierRepository, times(1)).findAll();
        verify(courierRepository, times(0)).findByActiveTrue();
        verify(courierTransformer, times(0)).toCourier(any());
    }

    @Test
    void test_shouldUpdateCourier_ByCorrectId() {
        long courierId = 1L;
        CourierEntity storedCourier = CourierEntity.builder()
                .id(courierId)
                .firstName("FirstName")
                .lastName("LastName")
                .active(true)
                .build();
        CourierRequestDto courierRequestDto = CourierRequestDto.builder()
                .firstName("NewFirstName")
                .lastName("NewLastName")
                .active(true)
                .build();
        CourierEntity courierEntityToStore = CourierEntity.builder()
                .id(courierId)
                .firstName(courierRequestDto.getFirstName())
                .lastName(courierRequestDto.getLastName())
                .active(courierRequestDto.isActive())
                .build();
        Courier expectedCourier = Courier.builder()
                .id(storedCourier.getId())
                .name(String.format("%s %s", courierRequestDto.getFirstName(), courierRequestDto.getLastName()))
                .active(courierRequestDto.isActive())
                .build();
        when(courierRepository.findById(courierId)).thenReturn(Optional.of(storedCourier));
        when(courierTransformer.toCourier(any())).thenCallRealMethod();
        when(courierRepository.save(courierEntityToStore)).thenReturn(courierEntityToStore);

        Courier actualUpdatedCourier = courierService.updateCourierById(courierId, courierRequestDto);
        assertEquals(expectedCourier, actualUpdatedCourier);
        verify(courierRepository, times(1)).findById(courierId);
        verify(courierRepository, times(1)).save(courierEntityToStore);
        verify(courierTransformer, times(1)).toCourier(courierEntityToStore);
    }

    @Test
    void test_shouldThrowException_whenUpdateCourier_WithIdNotExist() {
        when(courierRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(ApiCustomException.class, () -> courierService.updateCourierById(1L, CourierRequestDto.builder().build()));
        verify(courierRepository, times(1)).findById(any());
        verify(courierRepository, times(0)).save(any());
        verify(courierTransformer, times(0)).toCourier(any());
    }

    private List<CourierEntity> getCourierEntityList() {
        return Arrays.asList(
                CourierEntity.builder().id(1).firstName("1fristName").lastName("1lastName").active(true).build(),
                CourierEntity.builder().id(2).firstName("2firstName").lastName("2lastName").active(true).build(),
                CourierEntity.builder().id(3).firstName("3firstName").lastName("3lastName").active(false).build()
        );
    }
}