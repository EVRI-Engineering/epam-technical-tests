package com.evri.interview.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.evri.interview.model.Courier;
import com.evri.interview.model.CourierPutDataModel;
import com.evri.interview.repository.CourierEntity;
import com.evri.interview.repository.CourierRepository;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@Sql(value = "/sql/test_data.sql", executionPhase = BEFORE_TEST_METHOD)
@Sql(value = "/sql/clean.sql", executionPhase = AFTER_TEST_METHOD)
public class CourierIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private CourierRepository courierRepository;

    @Test
    void shouldResponse_WhenGetForAllCouriers() {

        final ResponseEntity<List<Courier>> response = testRestTemplate.exchange("http://localhost:" + port + "/api/couriers", GET, null,
            new ParameterizedTypeReference<List<Courier>>() {
            });

        assertThat(response.getBody())
            .containsExactly(
                Courier.builder().id(1).name("Ben Askew").active(true).build(),
                Courier.builder().id(2).name("Jonh Robertz").active(false).build()
            );
    }

    @Test
    void shouldResponse_WhenGetForActiveCouriers() {

        final UriComponents uri = UriComponentsBuilder.fromHttpUrl("http://localhost:" + port + "/api/couriers")
            .queryParam("isActive", true)
            .build();

        final ResponseEntity<List<Courier>> response = testRestTemplate.exchange(uri.toUriString(), GET, null,
            new ParameterizedTypeReference<List<Courier>>() {
            }
        );

        assertThat(response.getBody())
            .containsExactly(
                Courier.builder().id(1).name("Ben Askew").active(true).build()
            );
    }

    @Test
    void shouldResponse200_WhenPutCourierData() {

        testRestTemplate.put("http://localhost:" + port + "/api/couriers/1", aCourierPutDataModel());
        assertThat(courierRepository.findById(1L))
            .contains(aCourier());
    }

    private CourierEntity aCourier() {

        return CourierEntity.builder()
            .id(1L)
            .firstName("Max")
            .lastName("Gilbert")
            .active(true)
            .build();
    }

    private CourierPutDataModel aCourierPutDataModel() {

        return new CourierPutDataModel("Max", "Gilbert", true);
    }


}
