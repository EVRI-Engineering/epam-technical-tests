package com.evri.interview.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


@DataJpaTest
@Sql(scripts = {"classpath:cleanup.sql", "classpath:test-data.sql"})
class CourierRepositoryTest {

    @Autowired
    private CourierRepository courierRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllByActivityActive() {
        List<CourierEntity> couriersByActivity = courierRepository.findAllByActivity(true);

        assertFalse(couriersByActivity.isEmpty());
        assertTrue(couriersByActivity.stream().allMatch(CourierEntity::isActive));
    }

    @Test
    void getAllByActivityInactive() {
        List<CourierEntity> couriersByActivity = courierRepository.findAllByActivity(false);

        assertFalse(couriersByActivity.isEmpty());
        assertTrue(couriersByActivity.stream().noneMatch(CourierEntity::isActive));
    }
}
