package com.evri.interview.service;

import com.evri.interview.exception.EntityNotFoundException;
import com.evri.interview.model.Courier;
import com.evri.interview.repository.CourierEntity;
import com.evri.interview.repository.CourierRepository;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CourierServiceTest {

    @Mock
    private CourierRepository courierRepository;
    @Spy
    private CourierTransformer courierTransformer;
    @InjectMocks
    private CourierService courierService;

    @Test
    void shouldCallFindAllCouriers_WhenActiveFlagIsFalse() {
        //given
        //when
        when(courierRepository.findAll())
                .thenReturn(Lists.newArrayList(createCourier(10L, true),
                        createCourier(11L, false)));

        List<Courier> couriers = courierService.getAllCouriers(false);
        //then
        verify(courierRepository).findAll();
        assertThat(couriers).hasSize(2);
    }

    @Test
    void shouldCallFindAllCouriers_WhenActiveFlagIsTrue() {
        //given
        when(courierRepository.findAll())
                .thenReturn(Lists.newArrayList(createCourier(10L, true),
                        createCourier(11L, false)));
        //when
        List<Courier> couriers = courierService.getAllCouriers(true);
        //then
        verify(courierRepository).findAll();
        assertThat(couriers).hasSize(1);
    }

    @Test
    void shouldUpdateExistedCourier() {
        //given
        long id = 11L;
        CourierEntity courier = createCourier(id, true);
        when(courierRepository.findById(eq(id)))
                .thenReturn(Optional.of(courier));
        //when
        long courierId = courierService.updateCourier(id, courierTransformer.toCourier(courier));

        //then
        assertEquals(courierId, id);
    }

    @Test
    void shouldThrowExceptionIfCourierNotExists() {
        //given
        long id = 10L;
        CourierEntity courier = createCourier(id, true);
        when(courierRepository.findById(eq(id)))
                .thenReturn(Optional.empty());
        //when
        assertThrows(EntityNotFoundException.class,
                () -> courierService.updateCourier(id, courierTransformer.toCourier(courier)));
    }

    private CourierEntity createCourier(long id, boolean active) {

        return CourierEntity.builder()
                .id(id)
                .firstName("Alex")
                .lastName("Sidorov")
                .active(active)
                .build();
    }
}
