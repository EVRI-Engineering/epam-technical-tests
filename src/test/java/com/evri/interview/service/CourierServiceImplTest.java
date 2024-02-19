package com.evri.interview.service;

import com.evri.interview.dto.CourierResponseDto;
import com.evri.interview.dto.CourierUpdateDto;
import com.evri.interview.exception.EntityNotFoundException;
import com.evri.interview.mapper.CourierMapper;
import com.evri.interview.model.CourierEntity;
import com.evri.interview.repository.CourierRepository;
import com.evri.interview.service.impl.CourierServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CourierServiceImplTest {
    @Spy
    CourierMapper courierMapper;

    @Mock
    CourierRepository courierRepository;

    @InjectMocks
    CourierServiceImpl courierService;

    @Test
    public void testGetAllActiveCouriers() {
        CourierEntity entity = new CourierEntity(1L, "Joyce", "Barton", true);

        when(courierRepository.findByActive(true)).thenReturn(Collections.singletonList(entity));

        List<CourierResponseDto> result = courierService.getAllCouriers(true);

        assertEquals(result.size(), 1);
        assertEquals(result.get(0).getId(), 1L);
        assertEquals(result.get(0).getName(), "Joyce Barton");
        assertTrue(result.get(0).isActive());
        verify(courierRepository, times(1)).findByActive(true);
        verify(courierMapper, times(1)).toCourierResponseDto(any());
    }

    @Test
    public void testGetAllCouriers() {
        CourierEntity entity = new CourierEntity(1L, "Joyce", "Barton", false);

        when(courierRepository.findAll()).thenReturn(Collections.singletonList(entity));

        List<CourierResponseDto> result = courierService.getAllCouriers(false);

        assertEquals(result.size(), 1);
        assertEquals(result.get(0).getId(), 1L);
        assertEquals(result.get(0).getName(), "Joyce Barton");
        assertFalse(result.get(0).isActive());
        verify(courierRepository, times(1)).findAll();
        verify(courierMapper, times(1)).toCourierResponseDto(any());
    }

    @Test
    public void testUpdateCourierById() {
        CourierEntity entity = new CourierEntity(1L, "Joyce", "Barton", true);
        CourierUpdateDto courierUpdateDto = new CourierUpdateDto("Summer", "Hunter", false);
        CourierEntity updatedCourierEntity = new CourierEntity(1L, "Summer", "Hunter", false);

        when(courierRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(courierRepository.save(any())).thenReturn(updatedCourierEntity);

        CourierResponseDto result = courierService.updateCourierById(1L, courierUpdateDto);

        assertNotNull(result);
        assertEquals(result.getId(), 1L);
        assertEquals(result.getName(), "Summer Hunter");
        assertFalse(result.isActive());
        verify(courierRepository, times(1)).findById(1L);
        verify(courierMapper, times(1)).updateCourierEntity(entity, courierUpdateDto);
        verify(courierRepository, times(1)).save(entity);
        verify(courierMapper, times(1)).toCourierResponseDto(entity);
    }

    @Test
    public void testUpdateCourierById_EntityNotFound() {
        when(courierRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> courierService.updateCourierById(1L, CourierUpdateDto.builder().build()));
        verify(courierRepository, times(1)).findById(1L);
        verify(courierMapper, never()).updateCourierEntity(any(), any());
        verify(courierRepository, never()).save(any());
        verify(courierMapper, never()).toCourierResponseDto(any());
    }
}
