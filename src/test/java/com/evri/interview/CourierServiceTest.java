package com.evri.interview;

import com.evri.interview.exception.ValidationException;
import com.evri.interview.model.Courier;
import com.evri.interview.repository.CourierRepository;
import com.evri.interview.service.CourierService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class CourierServiceTest {

    @Autowired
    private CourierService service;

    @Autowired
    private CourierRepository repository;

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    @DisplayName("Retrieve all couriers by status.")
    void getAllCouriersWithActiveStatuses(boolean isActive) {
        List<Courier> couriers = service.getAllCouriers(isActive);
        assertNotNull(couriers);
        couriers.forEach(el -> assertEquals(isActive, el.isActive()));
    }

    @Test
    @DisplayName("Update unknown entity and get exception.")
    void updateUnknownEntity() {
        Courier courier = Courier.builder().name("wrong name").active(false).build();
        assertThrows(ValidationException.class,
                () -> service.update(20, courier));
    }

    @Test
    @DisplayName("Update known entity.")
    void updateKnownEntity() {
        service.update(1,
                Courier.builder().name("Red Bull").active(false).build());

        repository.findById(1L).ifPresent(entity -> {
            assertEquals(1, entity.getId());
            assertFalse(entity.isActive());
            assertEquals("Red", entity.getFirstName());
            assertEquals("Bull", entity.getLastName());
        });

    }
}
