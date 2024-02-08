package com.evri.interview;

import com.evri.interview.controller.CourierController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class ApplicationTest {

    @Autowired
    private CourierController courierController;

    @Test
    @DisplayName("Sanity test")
    void contextLoads() {
        assertNotNull(courierController);
    }
}
