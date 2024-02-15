package com.evri.interview.service;

import com.evri.interview.dto.CourierRequestDto;
import com.evri.interview.dto.CourierResponseDto;
import com.evri.interview.exception.RecordNotFoundException;
import com.evri.interview.mapper.CourierTransformer;
import com.evri.interview.model.CourierEntity;
import com.evri.interview.repository.CourierRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourierServiceTest {

    private CourierService courierService;

    @Mock
    private CourierRepository courierRepository;

    @Spy
    private CourierTransformer courierTransformer = new CourierTransformer();

    @BeforeEach
    public void init() {
        courierService = new CourierService(courierTransformer, courierRepository);
    }

    @Test
    public void shouldReturnAllCouriers_whenIsActiveFalse() {

        CourierEntity entity = CourierEntity.builder()
                .id(1)
                .active(false)
                .firstName("Jane")
                .lastName("Doe")
                .build();

        CourierResponseDto courierResponseDto = CourierResponseDto.builder()
                .id(1)
                .active(false)
                .name("Jane Doe")
                .build();
        List<CourierResponseDto> expected = Collections.singletonList(courierResponseDto);

        when(courierRepository.findAll()).thenReturn(Collections.singletonList(entity));

        List<CourierResponseDto> actual = courierService.getAllCouriers(false);

        verify(courierRepository).findAll();
        verify(courierRepository, never()).findByActiveTrue();
        verify(courierTransformer).toCourier(entity);

        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnOnlyActiveCouriers_whenIsActiveTrue() {

        CourierEntity entity = CourierEntity.builder()
                .id(1)
                .active(true)
                .firstName("Jane")
                .lastName("Doe")
                .build();

        CourierResponseDto courierResponseDto = CourierResponseDto.builder()
                .id(1)
                .active(true)
                .name("Jane Doe")
                .build();
        List<CourierResponseDto> expected = Collections.singletonList(courierResponseDto);

        when(courierRepository.findByActiveTrue()).thenReturn(Collections.singletonList(entity));

        List<CourierResponseDto> actual = courierService.getAllCouriers(true);

        verify(courierRepository).findByActiveTrue();
        verify(courierRepository, never()).findAll();
        verify(courierTransformer).toCourier(entity);

        assertEquals(expected, actual);
    }

    @Test
    public void throwsRecordNotFoundException_whenIdDoesNotExist() {
        CourierRequestDto courierRequestDto = new CourierRequestDto();
        when(courierRepository.existsById(eq(1L))).thenReturn(false);

        RecordNotFoundException recordNotFoundException = assertThrows(RecordNotFoundException.class,
                () -> courierService.updateCourierById(1, courierRequestDto)
        );
        assertEquals("Courier with id [1] is not found", recordNotFoundException.getMessage());

        verify(courierTransformer, never()).toCourierEntity(courierRequestDto);
        verify(courierRepository, never()).save(any());

    }


    @Test
    public void shouldUpdateRecord_whenCourierExists() {

        CourierEntity entity = CourierEntity.builder()
                .id(1)
                .active(true)
                .firstName("Jane")
                .lastName("Doe")
                .build();

        CourierRequestDto requestDto = new CourierRequestDto();
        requestDto.setActive(true);
        requestDto.setFirstName("Jane");
        requestDto.setLastName("Doe");

        CourierResponseDto expected = CourierResponseDto.builder()
                .id(1)
                .active(true)
                .name("Jane Doe")
                .build();

        when(courierRepository.existsById(1L)).thenReturn(true);
        when(courierRepository.save(entity)).thenReturn(entity);

        CourierResponseDto actual = courierService.updateCourierById(1, requestDto);

        verify(courierTransformer).toCourierEntity(requestDto);
        verify(courierTransformer).toCourier(entity);
        verify(courierRepository).save(entity);
        verify(courierRepository).existsById(1L);

        assertEquals(expected, actual);
    }

    @Test
    public void shouldIgnoreIdFromBody_whenUpdateIsDone() {

        CourierEntity entity = CourierEntity.builder()
                .id(1)
                .active(true)
                .firstName("Jane")
                .lastName("Doe")
                .build();

        CourierRequestDto requestDto = new CourierRequestDto();
        requestDto.setId(55);
        requestDto.setActive(true);
        requestDto.setFirstName("Jane");
        requestDto.setLastName("Doe");

        CourierResponseDto expected = CourierResponseDto.builder()
                .id(1)
                .active(true)
                .name("Jane Doe")
                .build();

        when(courierRepository.existsById(1L)).thenReturn(true);
        when(courierRepository.save(entity)).thenReturn(entity);

        CourierResponseDto actual = courierService.updateCourierById(1, requestDto);

        verify(courierTransformer).toCourierEntity(requestDto);
        verify(courierTransformer).toCourier(entity);
        verify(courierRepository).save(entity);
        verify(courierRepository).existsById(1L);

        assertEquals(expected, actual);

    }

}