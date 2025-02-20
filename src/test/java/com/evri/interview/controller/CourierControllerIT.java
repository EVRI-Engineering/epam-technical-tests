package com.evri.interview.controller;// Интеграционные тесты
import com.evri.interview.model.Courier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static com.evri.interview.config.CourierTestUtils.API_URL;
import static com.evri.interview.config.CourierTestUtils.COURIERS_API;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CourierControllerIT {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl;

    @BeforeEach
    void setUp() {
        baseUrl = API_URL + port + COURIERS_API;
    }

    @Test
    @DisplayName("Get all couriers")
    void testGetAllCouriers() {
        ResponseEntity<List> response = restTemplate.getForEntity(baseUrl, List.class);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    @DisplayName("Update сourier")
    @Sql(scripts = "/init-couriers.sql")
    void testUpdateCourier() {
        Courier courier = Courier.builder()
                .id(1L)
                .name("Ben Askew")
                .active(true).build();

        HttpEntity<Courier> request = new HttpEntity<>(courier);
        ResponseEntity<Courier> response = restTemplate.exchange(baseUrl + "/1", HttpMethod.PUT, request, Courier.class);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
    }
}