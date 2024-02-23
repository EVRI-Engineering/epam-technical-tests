package com.evri.interview;

import com.evri.interview.exception.ErrorResponse;
import com.evri.interview.model.Courier;
import com.evri.interview.model.CourierRequestDto;
import com.evri.interview.service.CourierService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class ApplicationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private MockMvc mockMvc;

    private final String baseUrl = "http://localhost:" + port + "/api";

    private final String couriersEndpointUrl = "/v1/couriers";

    @Autowired
    private CourierService courierService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @SneakyThrows
    @Test
    void test_shouldResponseOk_forGetAllCouriersEndpoint_withDefaultIsActive() {
        List<Courier> allCouriers = courierService.getAllCouriers(false);
        mockMvc.perform(
                        get(baseUrl + couriersEndpointUrl))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(allCouriers)));
    }

    @SneakyThrows
    @Test
    void test_shouldResponseOk_forGetAllActiveCouriersEndpoint() {
        List<Courier> allCouriers = courierService.getAllCouriers(true);
        mockMvc.perform(
                        get(baseUrl + couriersEndpointUrl)
                                .queryParam("isActive", "true"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(allCouriers)));
    }

    @SneakyThrows
    @Test
    void test_shouldReturnOk_forUpdateCourierById_withValidRequest() {
        long courierId = 1;
        CourierRequestDto courierRequestDto = CourierRequestDto.builder()
                .firstName("NewFirstName")
                .lastName("NewLastName")
                .active(true)
                .build();
        Courier expectedCourier = Courier.builder()
                .id(1)
                .name(String.format("%s %s", courierRequestDto.getFirstName(), courierRequestDto.getLastName()))
                .active(true)
                .build();
        mockMvc.perform(
                        put(baseUrl + couriersEndpointUrl + "/" + courierId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(courierRequestDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedCourier)));
    }

    @SneakyThrows
    @Test
    void test_shouldReturnException_forUpdateCourierById_withNonValidRequest() {
        long courierId = 1;
        CourierRequestDto courierRequestDto = CourierRequestDto.builder()
                .lastName("")
                .active(true)
                .build();
        mockMvc.perform(
                        put(baseUrl + couriersEndpointUrl + "/" + courierId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(courierRequestDto)))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void test_shouldReturnNotFound_forUpdateCourierById_withNonValidCourierId() {
        long courierId = 999;
        CourierRequestDto courierRequestDto = CourierRequestDto.builder()
                .firstName("NewFirstName")
                .lastName("NewLastName")
                .active(true)
                .build();
        ErrorResponse expectedErrorResponse = ErrorResponse.builder()
                .httpStatus(HttpStatus.NOT_FOUND)
                .message(String.format("Courier with id %s is not found", courierId))
                .timestamp(LocalDateTime.now())
                .build();
        mockMvc.perform(
                        put(baseUrl + couriersEndpointUrl + "/" + courierId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(courierRequestDto)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.httpStatus").value(expectedErrorResponse.getHttpStatus().name()))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.message").value(expectedErrorResponse.getMessage()));
    }

}
