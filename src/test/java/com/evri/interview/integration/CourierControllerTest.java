package com.evri.interview.integration;

import com.evri.interview.model.Courier;
import com.evri.interview.repository.CourierEntity;
import com.evri.interview.repository.CourierRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static com.evri.interview.CourierUtils.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@AutoConfigureMockMvc
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CourierControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CourierRepository courierRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Order(1)
    void shouldReturnAllCouriers() throws Exception {
        mockMvc
                .perform(get("/api/couriers"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @Order(2)
    void shouldReturnActiveCouriersWhenIsActiveFlagTrue() throws Exception {
        Courier courier = courier();

        mockMvc
                .perform(get("/api/couriers?isActive=true"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(courier.getId()))
                .andExpect(jsonPath("$[0].name").value(courier.getName()))
                .andExpect(jsonPath("$[0].active").value(true));
    }

    @Test
    @Order(3)
    void shouldReturnInactiveCouriersWhenIsActiveFlagFalse() throws Exception {
        mockMvc
                .perform(get("/api/couriers?isActive=false"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(INACTIVE_COURIER_ID))
                .andExpect(jsonPath("$[0].name").value(INACTIVE_COURIER_FIRST_NAME + " " + INACTIVE_COURIER_LAST_NAME))
                .andExpect(jsonPath("$[0].active").value(false));
    }

    @Test
    @Order(4)
    void shouldUpdateCourier() throws Exception {
        Courier courier = courier();
        courier.setName("Updated Name");

        mockMvc.perform(
                        put("/api/couriers/" + courier.getId())
                                .content(objectMapper.writeValueAsString(courier))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(courier.getName()));

        Optional<CourierEntity> actual = courierRepository.findById(courier.getId());
        assertThat(actual.get().getFirstName()).isEqualTo("Updated");
        assertThat(actual.get().getLastName()).isEqualTo("Name");
    }

    @Test
    @Order(5)
    void shouldReturnNotFoundWhenUpdatingWithNonExistingId() throws Exception {
        Courier courier = courier();
        courier.setId(999L);

        mockMvc.perform(
                        put("/api/couriers/" + courier.getId())
                                .content(objectMapper.writeValueAsString(courier))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
