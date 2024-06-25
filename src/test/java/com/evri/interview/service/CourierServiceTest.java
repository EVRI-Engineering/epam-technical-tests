package com.evri.interview.service;

import com.evri.interview.model.Courier;
import com.evri.interview.model.CourierDTO;
import com.evri.interview.repository.CourierEntity;
import com.evri.interview.repository.CourierRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CourierServiceTest {
    @Mock
    CourierTransformer courierTransformer;
    @Mock
    CourierRepository repository;

    @InjectMocks
    CourierService service;

    @Test
    void getAllCouriersWithActiveStatusWhenActiveStatusTrue() {
        CourierEntity entity1 = CourierEntity.builder().build();
        CourierEntity entity2 = CourierEntity.builder().build();
        Courier courier1 = Courier.builder().build();
        Courier courier2 = Courier.builder().build();
        List<CourierEntity> responseFromDb = Arrays.asList(entity1,entity2);

        when(courierTransformer.toCourier(same(entity1))).thenReturn(courier1);
        when(courierTransformer.toCourier(same(entity2))).thenReturn(courier2);
        when(repository.findByActiveIs(true)).thenReturn(responseFromDb);

        List<Courier> result = service.getAllCouriersWithActiveStatus(true);

        assertEquals(2, result.size());
        assertSame(courier1, result.get(0));
        assertSame(courier2, result.get(1));
    }

    @Test
    void getAllCouriersWithActiveStatusWhenActiveStatusFalse() {
        CourierEntity entity1 = CourierEntity.builder().build();
        Courier courier1 = Courier.builder().build();
        List<CourierEntity> responseFromDb = Arrays.asList(entity1);

        when(courierTransformer.toCourier(same(entity1))).thenReturn(courier1);
        when(repository.findByActiveIs(false)).thenReturn(responseFromDb);

        List<Courier> result = service.getAllCouriersWithActiveStatus(false);

        assertEquals(1, result.size());
        assertSame(courier1, result.get(0));
    }

    @Test
    void updateCourierUpdatesCourierWhenIdExists() {
        final CourierDTO dto = CourierDTO.builder().build();
        final CourierEntity entity = CourierEntity.builder().build();
        final long id = 1;
        when(repository.existsById(id)).thenReturn(true);
        when(courierTransformer.toCourierEntity(id,dto)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        Optional<CourierDTO> result = service.updateCourier(id,dto);
        assertTrue(result.isPresent());
        assertSame(dto, result.get());
    }

    @Test
    void updateCourierReturnsEmptyOptionalWhenIdNotExists() {
        final CourierDTO dto = CourierDTO.builder().build();
        final long id = 1;
        when(repository.existsById(id)).thenReturn(false);
        Optional<CourierDTO> result = service.updateCourier(id,dto);
        assertFalse(result.isPresent());
    }
}
