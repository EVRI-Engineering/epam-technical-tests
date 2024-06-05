package com.evri.interview.service;

import com.evri.interview.model.Courier;
import com.evri.interview.repository.CourierEntity;
import com.evri.interview.repository.CourierRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class CourierServiceTest {

    private String courierFullNameFormat = "%s %s";

    @Autowired
    private CourierService courierService;

    @MockBean
    private CourierRepository repository;

    @Test
    void getAllCouriers() {
        List<CourierEntity> allCouriersEntities = Arrays.asList(
                CourierEntity.builder().id(1).firstName("John").lastName("Snow").active(true).build(),
                CourierEntity.builder().id(2).firstName("Rob").lastName("Stark").active(true).build()
        );

        when(repository.findAll()).thenReturn(allCouriersEntities);

        List<Courier> actualCouriers = courierService.getAllCouriers();

        actualCouriers.forEach(courierDto -> {
            Optional<CourierEntity> courierEntity = allCouriersEntities.stream()
                    .filter(entity -> entity.getId() == courierDto.getId()).findFirst();

            assertNotNull(courierEntity, "Entity is missed for courier " + courierDto.getId());
            assertEquals(courierDto.isActive(), courierEntity.get().isActive());
            assertEquals(courierDto.getName(),
                    String.format(courierFullNameFormat, courierEntity.get().getFirstName(), courierEntity.get().getLastName()));
        });
        verify(repository).findAll();
    }

    @Test
    void getAllCouriersByActivityActive() {
        List<CourierEntity> allCouriersEntities = Arrays.asList(
            CourierEntity.builder().id(1).firstName("John").lastName("Snow").active(true).build(),
            CourierEntity.builder().id(2).firstName("Rob").lastName("Stark").active(true).build()
        );

        when(repository.findAllByActivity(true)).thenReturn(allCouriersEntities);

        List<Courier> actualCouriers = courierService.getAllCouriersByActivity(true);

        actualCouriers.forEach(courierDto -> {
            Optional<CourierEntity> courierEntity = allCouriersEntities.stream()
                    .filter(entity -> entity.getId() == courierDto.getId()).findFirst();

            assertNotNull(courierEntity, "Entity is missed for courier " + courierDto.getId());
            assertEquals(courierDto.isActive(), courierEntity.get().isActive());
            assertEquals(courierDto.getName(),
                    String.format(courierFullNameFormat, courierEntity.get().getFirstName(), courierEntity.get().getLastName()));
        });
        verify(repository).findAllByActivity(true);
    }

    @Test
    void getAllCouriersByActivityInactive() {
        List<CourierEntity> allCouriersEntities = Arrays.asList(
                CourierEntity.builder().id(1).firstName("John").lastName("Snow").active(false).build(),
                CourierEntity.builder().id(2).firstName("Rob").lastName("Stark").active(false).build()
        );

        when(repository.findAllByActivity(true)).thenReturn(allCouriersEntities);

        List<Courier> actualCouriers = courierService.getAllCouriersByActivity(false);

        actualCouriers.forEach(courierDto -> {
            Optional<CourierEntity> courierEntity = allCouriersEntities.stream()
                    .filter(entity -> entity.getId() == courierDto.getId()).findFirst();

            assertNotNull(courierEntity, "Entity is missed for courier " + courierDto.getId());
            assertEquals(courierDto.isActive(), courierEntity.get().isActive());
            assertEquals(courierDto.getName(),
                    String.format(courierFullNameFormat, courierEntity.get().getFirstName(), courierEntity.get().getLastName()));
        });
        verify(repository).findAllByActivity(false);
    }
}
