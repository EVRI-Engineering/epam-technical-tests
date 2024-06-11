package com.evri.interview.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.evri.interview.dto.CourierDto;
import com.evri.interview.model.Courier;
import com.evri.interview.service.CourierService;
import com.fasterxml.jackson.databind.ObjectMapper;

@AutoConfigureMockMvc
@WebMvcTest
@ExtendWith({ SpringExtension.class, MockitoExtension.class })
public class CourierControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CourierService courierService;

    @Test
    public void givenSomeActiveCouriers_whenUserAsksForAllCouriers_thenAllCouriersAreReturned() throws Exception {
        Mockito.when(courierService.getCouriers(false))
                .thenReturn(Arrays.asList(Courier.builder().name("John Doe").active(true).build(),
                        Courier.builder().name("Don Joe").active(false).build()));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/couriers"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name", is("John Doe")))
                .andExpect(jsonPath("$[0].active", is(true)))
                .andExpect(jsonPath("$[1].name", is("Don Joe")))
                .andExpect(jsonPath("$[1].active", is(false)))
                .andExpect(jsonPath("$", hasSize(equalTo(2))));
    }

    @Test
    public void givenSomeActiveCouriers_whenUserAsksForOnlyActiveCouriers_thenOnlyActiveCouriersAreReturned()
            throws Exception {
        Mockito.when(courierService.getCouriers(true))
                .thenReturn(Arrays.asList(Courier.builder().name("John Doe").active(true).build(),
                        Courier.builder().name("Don Joe").active(true).build()));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/couriers?onlyActive=true"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name", is("John Doe")))
                .andExpect(jsonPath("$[0].active", is(true)))
                .andExpect(jsonPath("$[1].name", is("Don Joe")))
                .andExpect(jsonPath("$[1].active", is(true)))
                .andExpect(jsonPath("$", hasSize(equalTo(2))));
    }

    @Test
    public void givenSomeExistingCourier_whenUserWantsToUpdateThatCourier_thenCourierIsUpdated() throws Exception {
        Mockito.when(
                courierService.updateCourier(Mockito.anyLong(), Mockito.any(), Mockito.any(), Mockito.anyBoolean()))
                .thenAnswer(args -> Optional.of(Courier.builder()
                        .id(args.getArgument(0))
                        .name(args.getArgument(1) + " " + args.getArgument(2))
                        .active(args.getArgument(3))
                        .build()));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/couriers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(
                        CourierDto.builder().firstName("first").lastName("last").active(true).build())))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("first last")))
                .andExpect(jsonPath("$.active", is(true)));
    }
}
