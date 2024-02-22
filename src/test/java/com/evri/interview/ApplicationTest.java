package com.evri.interview;

import com.evri.interview.controller.CourierController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ApplicationTest {

    @Autowired
    CourierController sut;

    @Test
    void contextLoads() {
        Assertions.assertNotNull(sut);
    }

}
