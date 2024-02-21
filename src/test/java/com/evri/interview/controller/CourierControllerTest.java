package com.evri.interview.controller;

import com.evri.interview.exception.EntityNotFoundException;
import com.evri.interview.model.Courier;
import com.evri.interview.service.CourierService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CourierController.class)
public class CourierControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourierService courierService;

    @Test
    public void shouldReturnAllCouriers() throws Exception {
        Courier courier1 = Courier.builder().id(1L).name("John Doe").active(true).build();
        Courier courier2 = Courier.builder().id(2L).name("Jane Doe").active(false).build();
        List<Courier> allCouriers = Arrays.asList(courier1, courier2);

        when(courierService.getAllCouriers()).thenReturn(allCouriers);

        mockMvc.perform(get("/api/couriers")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is(courier1.getName())))
                .andExpect(jsonPath("$[1].name", is(courier2.getName())));
    }

    @Test
    public void shouldReturnActiveCouriers() throws Exception {
        Courier courier = Courier.builder().id(1L).name("John Doe").active(true).build();
        List<Courier> activeCouriers = Collections.singletonList(courier);

        when(courierService.getActiveCouriers()).thenReturn(activeCouriers);

        mockMvc.perform(get("/api/couriers?isActive=true")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(courier.getName())));
    }

    @Test
    public void shouldUpdateCourier() throws Exception {
        Courier courier = Courier.builder().id(1L).name("John Doe Updated").active(true).build();

        when(courierService.updateCourierById(eq(courier), eq(1L))).thenReturn(courier);

        mockMvc.perform(put("/api/courier/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"name\": \"John Doe Updated\", \"active\": true}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(courier.getName())));
    }

    @Test
    public void shouldReturnNotFoundWhenCourierNotUpdated() throws Exception {
        Long courierId = 1L;
        Courier courierToUpdate = Courier.builder().id(courierId).name("John Doe Updated").active(true).build();
        String notFoundMessage = "Courier with ID: " + courierId + " not found";

        when(courierService.updateCourierById(eq(courierToUpdate), eq(courierId))).thenThrow(new EntityNotFoundException(Courier.class.getSimpleName(), courierId));
        mockMvc.perform(put("/api/courier/" + courierId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": " + courierId + ", \"name\": \"John Doe Updated\", \"active\": true}"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(notFoundMessage));
    }
}
