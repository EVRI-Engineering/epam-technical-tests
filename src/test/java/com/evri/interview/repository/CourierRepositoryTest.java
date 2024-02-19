package com.evri.interview.repository;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test")
class CourierRepositoryTest {

    @Autowired
    private CourierRepository repository;

    @Test
    @DisplayName("test fetching all couriers")
    @Sql(statements = {
            "INSERT INTO couriers (ID, FST_NME, LST_NME, ACTV) VALUES (1, 'Ben', 'Askew', 1)",
            "INSERT INTO couriers (ID, FST_NME, LST_NME, ACTV) VALUES (2, 'Ihor', 'Kiulian', 0)",
    })
    @Sql(statements = "DELETE FROM couriers", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void testFindAll() {
        List<CourierEntity> courierEntities = repository.findAll();

        assertFalse(courierEntities.isEmpty());
        assertEquals(2, courierEntities.size());
    }

    @Test
    @DisplayName("test fetching if db is empty")
    void testFindAllEmpty() {
        List<CourierEntity> courierEntities = repository.findAll();
        assertTrue(courierEntities.isEmpty());
    }

    @Test
    @DisplayName("test fetching only active couriers")
    @Sql(statements = {
            "INSERT INTO couriers (ID, FST_NME, LST_NME, ACTV) VALUES (1, 'Ben', 'Askew', 1)",
            "INSERT INTO couriers (ID, FST_NME, LST_NME, ACTV) VALUES (2, 'Ihor', 'Kiulian', 0)",
            "INSERT INTO couriers (ID, FST_NME, LST_NME, ACTV) VALUES (3, 'Test', 'Courier', 1)",
    })
    @Sql(statements = "DELETE FROM couriers", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void testFindAllOnlyActive() {
        List<CourierEntity> courierEntities = repository.findByActiveTrue();

        assertFalse(courierEntities.isEmpty());
        assertEquals(2, courierEntities.size());

        boolean isAllActive = courierEntities.stream().allMatch(CourierEntity::isActive);
        assertTrue(isAllActive);
    }
}