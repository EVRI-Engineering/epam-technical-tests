package com.evri.interview.service;

import com.evri.interview.converter.CourierConverter;
import com.evri.interview.exception.CourierNotFoundException;
import com.evri.interview.model.Courier;
import com.evri.interview.repository.CourierRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.evri.interview.constants.PresetObjects.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CourierServiceTest {

    @Mock
    private CourierConverter transformer;
    @Mock
    private CourierRepository repository;

    @InjectMocks
    private CourierService sut;

    @Test
    void shouldGetActiveCouriers() {
        //Given
        when(repository.findByActiveTrue()).thenReturn(Collections.singletonList(ACTIVE_ENTITY));
        when(transformer.entityToCourier(any())).thenReturn(ACTIVE_DTO);

        //When
        List<Courier> actual = sut.getCouriersByActive(true);

        //Then
        assertThat(actual, hasItems(ACTIVE_DTO));
    }

    @Test
    void shouldGetInactiveCouriers() {
        //Given
        when(repository.findAll()).thenReturn(Arrays.asList(ACTIVE_ENTITY, INACTIVE_ENTITY));
        when(transformer.entityToCourier(eq(ACTIVE_ENTITY))).thenReturn(ACTIVE_DTO);
        when(transformer.entityToCourier(eq(INACTIVE_ENTITY))).thenReturn(INACTIVE_DTO);

        //When
        List<Courier> actual = sut.getCouriersByActive(false);

        //Then
        assertThat(actual, hasItems(ACTIVE_DTO, INACTIVE_DTO));
    }

    @Test
    void shouldSuccessfullyUpdateCourier() {
        //Given
        when(repository.findById(eq(1L))).thenReturn(Optional.of(ACTIVE_ENTITY));
        doReturn(ACTIVE_ENTITY).when(transformer).updateEntity(ACTIVE_ENTITY, UPDATE_DTO);

        //When
        //Then
        assertDoesNotThrow(() -> sut.updateCourier(1, UPDATE_DTO));
    }

    @Test
    void shouldThrowExceptionDuringUpdateCourier() {
        //Given
        when(repository.findById(eq(1L))).thenReturn(Optional.empty());

        //When
        //Then
        assertThrows(CourierNotFoundException.class, () -> sut.updateCourier(1, UPDATE_DTO));
    }

}