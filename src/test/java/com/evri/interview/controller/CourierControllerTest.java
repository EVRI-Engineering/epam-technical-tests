package com.evri.interview.controller;

import com.evri.interview.Application;
import com.evri.interview.dataprovider.DataProvider;
import com.evri.interview.model.Courier;
import com.evri.interview.repository.CourierRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
class CourierControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private CourierRepository courierRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("Get all couriers should return all couriers")
    @SneakyThrows
    void getAllCouriers_shouldGetTest() {
        mvc.perform(MockMvcRequestBuilders.get("/api/couriers"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].name").value("Ben Askew"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].active").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].name").value("Andrii Okhrymovych"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].active").value(false));
    }

    @Test
    @DisplayName("Get all active couriers should return only active couriers")
    @SneakyThrows
    void getActiveCouriers_shouldGetTest() {
        mvc.perform(MockMvcRequestBuilders.get("/api/couriers?isActive=true"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].name").value("Ben Askew"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].active").value(true));
    }

    @Test
    @DisplayName("If is active parameter is present and is false then should return all couriers")
    @SneakyThrows
    void getAllWhenIsActiveIsFalseCouriers_shouldGetAllTest() {
        mvc.perform(MockMvcRequestBuilders.get("/api/couriers?isActive=false"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].name").value("Ben Askew"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].active").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].name").value("Andrii Okhrymovych"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].active").value(false));
    }

    @Test
    @SneakyThrows
    @DisplayName("Update courier should successfully update")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void updateCourier_shouldUpdateTest() {
        Courier courier = DataProvider.getCouriers().get(0);
        courier.setName("UpdatedFirstName UpdatedLastName");
        mvc.perform(MockMvcRequestBuilders.put("/api/couriers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(courier)))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
        courierRepository.findById(1L)
                .ifPresent(courierEntity -> {
                    assertThat(courierEntity.getFirstName()).isEqualTo("UpdatedFirstName");
                    assertThat(courierEntity.getLastName()).isEqualTo("UpdatedLastName");
                });
    }

    @Test
    @SneakyThrows
    @DisplayName("Update courier when courier does not exist")
    void updateCourierWhenCourierDoesNotExist_shouldNotUpdateTest() {
        Courier courier = DataProvider.getCouriers().get(0);
        courier.setId(3);
        courier.setName("UpdatedFirstName UpdatedLastName");
        mvc.perform(MockMvcRequestBuilders.put("/api/couriers/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(courier)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
        courierRepository.findById(1L)
                .ifPresent(courierEntity -> {
                    assertThat(courierEntity.getFirstName()).isEqualTo("Ben");
                    assertThat(courierEntity.getLastName()).isEqualTo("Askew");
                });
    }
}
