package com.evri.interview;

import com.evri.interview.exception.ValidationException;
import com.evri.interview.model.Courier;
import com.evri.interview.service.CourierService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
class ControllerTest {

    @MockBean
    CourierService courierService;
    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("Retrieve all couriers with default active status")
    void getAllCouriersWithActiveFalse() throws Exception {
        when(courierService.getAllCouriers(false)).thenReturn(
                List.of(Courier.builder().active(false).name("name").id(1).build()));
        mockMvc.perform(get("/api/couriers")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("name"))
                .andExpect(jsonPath("$[0].active").value(false));
    }

    @Test
    @DisplayName("Retrieve all couriers with active status = true")
    void getAllCouriersWithActiveTrue() throws Exception {
        when(courierService.getAllCouriers(true)).thenReturn(
                List.of(Courier.builder().active(true).name("name").id(1).build()));
        mockMvc.perform(get("/api/couriers?isActive=true")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("name"))
                .andExpect(jsonPath("$[0].active").value(true));
    }

    @Test
    @DisplayName("Update unknown entity and throw HTTP CODE:404")
    void updateUnknownEntity() throws Exception {
        doThrow(new ValidationException("")).when(courierService).update(anyLong(), any(Courier.class));

        mockMvc.perform(put("/api/couriers/{courierId}", 20)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getContent(Courier.builder().active(true).name("Rob Green").build())))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Update known entity and return HTTP CODE:200")
    void updateKnownEntity() throws Exception {
        mockMvc.perform(put("/api/couriers/{courierId}", 20)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getContent(Courier.builder().active(true).name("Rob Green").build())))
                .andExpect(status().isOk());
    }

    private String getContent(Object obj) throws Exception {
        return new ObjectMapper().writeValueAsString(obj);
    }
}
