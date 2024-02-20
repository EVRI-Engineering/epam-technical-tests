package com.evri.interview;

import com.evri.interview.repository.CourierEntity;
import com.evri.interview.repository.CourierRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static com.evri.interview.constants.PresetObjects.*;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-integration.properties")
class APITest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private CourierRepository repository;

    @Test
    void shouldGetAllByDefault() throws Exception {
        mvc.perform(get("/api/couriers"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        jsonPath("$[0].id", is((int) ACTIVE_DTO.getId())),
                        jsonPath("$[0].name", is(ACTIVE_DTO.getName())),
                        jsonPath("$[0].active", is(ACTIVE_DTO.isActive())))
                .andExpectAll(
                        jsonPath("$[1].id", is((int) INACTIVE_DTO.getId())),
                        jsonPath("$[1].name", is(INACTIVE_DTO.getName())),
                        jsonPath("$[1].active", is(INACTIVE_DTO.isActive())));
    }

    @Test
    void shouldGetActiveByParameter() throws Exception {
        mvc.perform(get("/api/couriers").param("isActive", "true"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].active", is(true)));
    }

    @Test
    void shouldThrowExceptionResponse() throws Exception {
        String courierForUpdateStr = new ObjectMapper().writeValueAsString(UPDATE_DTO);
        mvc.perform(put("/api/courier/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(courierForUpdateStr))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Courier with id 3 does not exist"));
    }

    @Test
    void shouldUpdateCourierById() throws Exception {
        //Given
        String courierForUpdateStr = new ObjectMapper().writeValueAsString(UPDATE_DTO);

        //When
        mvc.perform(put("/api/courier/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(courierForUpdateStr))
                .andExpect(status().isOk());

        //Then
        Optional<CourierEntity> entity = repository.findById(1L);
        assertTrue(entity.isPresent());
        assertEquals(1, entity.get().getId());
        assertEquals(UPDATE_DTO.getFirstName(), entity.get().getFirstName());
        assertEquals(UPDATE_DTO.getLastName(), entity.get().getLastName());
        assertEquals(UPDATE_DTO.getActive(), entity.get().isActive());
    }

}
