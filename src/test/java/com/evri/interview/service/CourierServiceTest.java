package com.evri.interview.service;

import com.evri.interview.exception.EntityNotFoundException;
import com.evri.interview.model.Courier;
import com.evri.interview.repository.CourierEntity;
import com.evri.interview.repository.CourierRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    public void getCouriersWithIsActiveFalse() {
        CourierEntity inactiveCourier = new CourierEntity()
                .setId(1L)
                .setActive(false)
                .setFirstName("Inactive")
                .setLastName("Courier");

        when(courierRepository.findAll())
                .thenReturn(singletonList(inactiveCourier));

        List<Courier> result = courierService.getCouriers(false);
        assertCourier(result.get(0), 1L, "Inactive Courier");

        verify(courierTransformer).toCourier(inactiveCourier);
    }

    @Test
    public void getCouriersWithIsActiveTrue() {
        CourierEntity activeCourier = new CourierEntity()
                .setId(2L)
                .setActive(true)
                .setFirstName("Active")
                .setLastName("Courier");

        when(courierRepository.findByActiveTrue())
                .thenReturn(singletonList(activeCourier));

        List<Courier> result = courierService.getCouriers(true);
        assertCourier(result.get(0), 2L, "Active Courier");

        verify(courierTransformer).toCourier(activeCourier);
    }

    @Test
    public void updateCourier() {
        CourierEntity inactiveCourier = new CourierEntity()
                .setId(1L)
                .setActive(false)
                .setFirstName("Inactive")
                .setLastName("Courier");
        Courier expected = new Courier()
                .setId(1L)
                .setName("Active Courier")
                .setActive(true);

        when(courierRepository.existsById(1L))
                .thenReturn(true);

        Courier actual = courierService.updateCourier(1L, expected);

        assertEquals(actual, expected);
    }

    @Test
    public void updateCourierWhenEntityNotFound() {
        assertThrows(EntityNotFoundException.class, () -> courierService.updateCourier(1L, new Courier()
                .setId(1L)
                .setName("Active Courier")
                .setActive(true)));
    }

    private void assertCourier(Courier courier, long id, String name) {
        assertEquals(courier.getId(), id);
        assertEquals(courier.getName(), name);
    }
}