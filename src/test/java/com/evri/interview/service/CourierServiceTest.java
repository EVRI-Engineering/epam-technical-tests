package com.evri.interview.service;

import com.evri.interview.model.Courier;
import com.evri.interview.repository.CourierEntity;
import com.evri.interview.repository.CourierRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CourierServiceTest {
    @Mock
    private CourierTransformer courierTransformer;

    @Mock
    private CourierRepository repository;

    @InjectMocks
    private CourierService courierService;

    @BeforeEach
    public void init() {
        Mockito.lenient()
                .when(courierTransformer.toCourier(Mockito.any()))
                .thenAnswer(args -> Courier.builder()
                        .id(args.getArgument(0, CourierEntity.class).getId())
                        .name(args.getArgument(0, CourierEntity.class).getFirstName() + " "
                                + args.getArgument(0, CourierEntity.class).getLastName())
                        .active(args.getArgument(0, CourierEntity.class).isActive())
                        .build());
    }

    @Test
    public void testGetCouriers() {
        List<CourierEntity> entities = Arrays.asList(new CourierEntity(1L, "John", "Doe", true),
                new CourierEntity(2L, "Jane", "Doe", false));
        when(repository.findAll()).thenReturn(entities);

        List<Courier> couriers = courierService.getCouriers(false);

        assertEquals(2, couriers.size());
        assertCourier(couriers.get(0), 1L, "John Doe", true);
        assertCourier(couriers.get(1), 2L, "Jane Doe", false);
    }

    @Test
    public void testGetCouriersOnlyActive() {
        List<CourierEntity> entities = Arrays.asList(new CourierEntity(1L, "John", "Doe", true),
                new CourierEntity(2L, "Jane", "Doe", true));
        when(repository.findByActiveTrue()).thenReturn(entities);

        List<Courier> couriers = courierService.getCouriers(true);

        assertEquals(2, couriers.size());
        assertCourier(couriers.get(0), 1L, "John Doe", true);
        assertCourier(couriers.get(1), 2L, "Jane Doe", true);
    }

    private void assertCourier(Courier courier, Long id, String name, boolean active) {
        assertEquals(id, courier.getId());
        assertEquals(name, courier.getName());
        assertEquals(active, courier.isActive());
    }

    @Test
    public void testUpdateExistingCourier() {
        long courierId = 1L;
        String firstName = "John";
        String lastName = "Doe";
        boolean active = true;
        when(repository.findById(courierId))
                .thenReturn(Optional.of(new CourierEntity(courierId, firstName, lastName, false)));
        when(repository.save(Mockito.any())).thenAnswer(args -> args.getArgument(0));

        Optional<Courier> updatedCourier = courierService.updateCourier(courierId, firstName, lastName, active);

        assertEquals(
                Optional.of(Courier.builder().id(courierId).name(firstName + " " + lastName).active(active).build()),
                updatedCourier);
    }
    
    @Test
    public void testUpdateNonExistingCourier() {
        when(repository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        Optional<Courier> updatedCourier = courierService.updateCourier(1L, "first", "last", false);

        assertFalse(updatedCourier.isPresent());
    }
}
