package com.evri.interview.controller;

import com.evri.interview.exception.ApiCustomException;
import com.evri.interview.model.Courier;
import com.evri.interview.model.CourierRequestDto;
import com.evri.interview.service.CourierService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CourierController.class)
class CourierControllerTest {

    public static final String IS_ACTIVE_QUERY_PARAM = "isActive";
    private final String baseControllerUrl = "/api/v1/couriers";
    ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourierService courierService;

    @SneakyThrows
    @Test
    void test_shouldResponseOk_GetAllCouriers_withDefaultValueIsActive() {
        List<Courier> courierList = getCourierList();
        String courierListJson = objectMapper.writeValueAsString(courierList);
        when(courierService.getAllCouriers(false)).thenReturn(courierList);

        mockMvc.perform(
                        get(baseControllerUrl))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(courierListJson));
    }

    @SneakyThrows
    @Test
    void test_shouldResponseOk_getAllCouriers_onlyWithActiveIsTrue() {
        List<Courier> activeCourierList = getCourierList().stream().filter(Courier::isActive).collect(Collectors.toList());
        String courierListJson = objectMapper.writeValueAsString(activeCourierList);
        when(courierService.getAllCouriers(true)).thenReturn(activeCourierList);

        mockMvc.perform(
                        get(baseControllerUrl)
                                .queryParam(IS_ACTIVE_QUERY_PARAM, "true"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(courierListJson));
    }

    @SneakyThrows
    @Test
    void test_shouldResponseBadRequest_whenQueryParamIsActive_isWrong() {
        mockMvc.perform(
                        get(baseControllerUrl)
                                .queryParam(IS_ACTIVE_QUERY_PARAM, "wrong"))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void test_shouldResponseOk_andUpdateCourierById() {
        Courier courierToUpdate = Courier.builder().id(1).name(String.format("%s %s", "NewFirstName", "NewLastName")).active(true).build();
        CourierRequestDto newCourierRequest = CourierRequestDto.builder().firstName("NewFirstName").lastName("NewLastName").active(true).build();

        when(courierService.updateCourierById(courierToUpdate.getId(), newCourierRequest)).thenReturn(courierToUpdate);

        mockMvc.perform(
                        put(baseControllerUrl + "/" + courierToUpdate.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(newCourierRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(courierToUpdate)));
    }

    @SneakyThrows
    @Test
    void test_shouldResponseNotFound_whenUpdateCourierById_withNonExistingCourierId() {
        long courierId = 2L;
        CourierRequestDto newCourierRequest = CourierRequestDto.builder().firstName("NewFirstName").lastName("NewLastName").active(true).build();
        ApiCustomException expectedException = new ApiCustomException(String.format("Courier with id %s is not found", courierId));
        when(courierService.updateCourierById(courierId, newCourierRequest)).thenThrow(expectedException);

        mockMvc.perform(
                        put(baseControllerUrl + "/" + courierId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(newCourierRequest)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.httpStatus").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value(expectedException.getMessage()))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @SneakyThrows
    @Test
    void test_shouldResponseBadRequest_whenUpdateCourierById_withNonValidRequestObject() {
        Courier courierToUpdate = Courier.builder().id(1).name(String.format("%s %s", "NewFirstName", "NewLastName")).active(true).build();
        CourierRequestDto newCourierRequest = CourierRequestDto.builder().firstName("").lastName("").active(true).build();

        mockMvc.perform(
                        put(baseControllerUrl + "/" + courierToUpdate.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(newCourierRequest)))
                .andExpect(status().isBadRequest());

    }

    private List<Courier> getCourierList() {
        return Arrays.asList(
                Courier.builder().id(1).name("First").active(true).build(),
                Courier.builder().id(2).name("Second").active(true).build(),
                Courier.builder().id(3).name("Third").active(false).build()
        );
    }

}