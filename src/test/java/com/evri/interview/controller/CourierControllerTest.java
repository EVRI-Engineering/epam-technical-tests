package com.evri.interview.controller;

import com.evri.interview.Application;
import com.evri.interview.model.Courier;
import com.evri.interview.service.CourierService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = Application.class)
@AutoConfigureMockMvc
public class CourierControllerTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private CourierService courierServiceMock;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final Courier COURIER_1 = Courier.builder()
            .id(1L)
            .name("John Doe")
            .active(true)
            .build();

    @Before
    public void setUp() {
        Mockito.when(courierServiceMock.getAllCouriers())
                .thenReturn(List.of(COURIER_1));

        Mockito.when(courierServiceMock.updateCourier(any(), any()))
                .thenReturn(Optional.of(COURIER_1));
    }

    @Test
    public void getAllCouriers_returnsCouriersList() throws Exception {
        mvc.perform(get("/api/couriers"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[0].active").value(true));
    }

    @Test
    public void updateCourier_updateSuccessful() throws Exception {
        mvc.perform(put("/api/couriers/1")
                        .contentType((MediaType.APPLICATION_JSON))
                        .content(OBJECT_MAPPER.writeValueAsString(COURIER_1))
                )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.active").value(true));
    }

    @Test
    public void updateCourier_noCourierFound_returns404ClientError() throws Exception {
        Mockito.when(courierServiceMock.updateCourier(any(), any()))
                .thenReturn(Optional.empty());

        mvc.perform(put("/api/couriers/1")
                        .contentType((MediaType.APPLICATION_JSON))
                        .content(OBJECT_MAPPER.writeValueAsString(COURIER_1))
                )
                .andExpect(status().is4xxClientError());
    }
}
