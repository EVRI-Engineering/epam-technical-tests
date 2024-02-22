package com.evri.interview;

import com.evri.interview.exception.UpdateNonExistantCourierException;
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

import static com.evri.interview.TestDataProvider.getCourierDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class CourierServiceTest {

    @Autowired
    private CourierService service;

    @Autowired
    private CourierRepository repository;

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    @DisplayName("Find couriers by activity status")
    void getAllCouriersWithActiveStatuses(boolean isActive) {
        List<Courier> couriers = service.getAllActiveCouriers(isActive);
        assertNotNull(couriers);
        couriers.forEach(courier -> assertEquals(isActive, courier.isActive()));
    }

    @Test
    @DisplayName("Update existed courier")
    void updatedExistedCourier() {
        service.updateCourier(1, getCourierDto("Best", "Courier", false));

        repository.findById(1L).ifPresent(entity -> {
            assertEquals(1, entity.getId());
            assertEquals("Best", entity.getFirstName());
            assertEquals("Courier", entity.getLastName());
            assertFalse(entity.isActive());
        });
    }

    @Test
    @DisplayName("Update non existent courier and get exception")
    void updateNonexistentCourier() {
        assertThrows(UpdateNonExistantCourierException.class,
                () -> service.updateCourier(5, getCourierDto("unknown", "courier", true)));
    }
}
