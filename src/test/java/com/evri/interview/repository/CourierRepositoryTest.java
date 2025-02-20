package com.evri.interview.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;



import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
@Slf4j
@ExtendWith(SpringExtension.class)
@DataJpaTest
class CourierRepositoryTest {

    @Autowired
    private CourierRepository courierRepository;

    @BeforeEach
    void setUp() {
        courierRepository.deleteAll();
    }

    @Test
    void testGetAllByActive_ShouldReturnActiveCouriers() {
        log.debug("testGetAllByActive_ShouldReturnActiveCouriers");
        CourierEntity activeCourier = CourierEntity.builder()
                .firstName("John")
                .lastName("Doe")
                .active(true)
                .build();

        CourierEntity inactiveCourier = CourierEntity.builder()
                .firstName("Jane")
                .lastName("Smith")
                .active(false)
                .build();

        courierRepository.saveAll(Arrays.asList(activeCourier, inactiveCourier));

        List<CourierEntity> activeCouriers = courierRepository.getAllByActive(true);

        assertThat(activeCouriers).hasSize(1);
        assertThat(activeCouriers.get(0).getFirstName()).isEqualTo("John");
    }
}
