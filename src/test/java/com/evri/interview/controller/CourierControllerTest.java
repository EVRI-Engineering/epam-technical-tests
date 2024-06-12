package com.evri.interview.controller;

import com.evri.interview.controller.dto.CourierDto;
import com.evri.interview.controller.exception.CourierNotFoundException;
import com.evri.interview.model.Courier;
import com.evri.interview.service.CourierService;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CourierController.class)
class CourierControllerTest {

    private static final long ID_1 = 1L;
    private static final long ID_2 = 2L;

    private static final String FULL_NAME = "Ben Askew";
    private static final String FULL_NAME_2 = "Dan Caskew";
    private static final String COURIERS_PATH = "/api/couriers";
    private static final String COURIER_ID_PATH = COURIERS_PATH + "/{courierId}";
    private static final String IS_ACTIVE_PARAM = "isActive";

    @MockBean
    private CourierService courierService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void givenIsActive_whenGetAllCouriers_thenReturnOnlyActiveCouriers() throws Exception {
        // given
        Courier courier = Courier.builder().id(ID_1).name(FULL_NAME).active(true).build();
        List<Courier> couriers = new ArrayList<>();
        couriers.add(courier);

        given(courierService.getAllCouriers(true)).willReturn(couriers);

        // when-then
        mockMvc.perform(get(COURIERS_PATH)
                        .queryParam(IS_ACTIVE_PARAM, "true"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(FULL_NAME)))
                .andExpect(jsonPath("$[0].active", is(true)));
    }

    @Test
    void givenIsNotActive_whenGetAllCouriers_thenReturnAllCouriers() throws Exception {
        // given
        Courier courier1 = Courier.builder().id(ID_1).name(FULL_NAME).active(true).build();
        Courier courier2 = Courier.builder().id(ID_2).name(FULL_NAME_2).active(false).build();

        List<Courier> couriers = new ArrayList<>();
        couriers.add(courier1);
        couriers.add(courier2);

        given(courierService.getAllCouriers(false)).willReturn(couriers);

        // when-then
        mockMvc.perform(get(COURIERS_PATH)
                        .queryParam(IS_ACTIVE_PARAM, "false"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is(FULL_NAME)))
                .andExpect(jsonPath("$[0].active", is(true)))
                .andExpect(jsonPath("$[1].name", is(FULL_NAME_2)))
                .andExpect(jsonPath("$[1].active", is(false)));
    }

    @Test
    void givenNotIActiveParam_whenGetAllCouriers_thenReturnAllCouriers() throws Exception {
        // given
        Courier courier1 = Courier.builder().id(ID_1).name(FULL_NAME).active(true).build();
        Courier courier2 = Courier.builder().id(ID_2).name(FULL_NAME_2).active(false).build();

        List<Courier> couriers = new ArrayList<>();
        couriers.add(courier1);
        couriers.add(courier2);

        given(courierService.getAllCouriers(false)).willReturn(couriers);

        // when-then
        mockMvc.perform(get(COURIERS_PATH))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is(FULL_NAME)))
                .andExpect(jsonPath("$[0].active", is(true)))
                .andExpect(jsonPath("$[1].name", is(FULL_NAME_2)))
                .andExpect(jsonPath("$[1].active", is(false)));
    }

    @Test
    void givenValidId_whenUpdateCourier_thenReturnUpdatedCourier() throws Exception {
        // given
        CourierDto dto = CourierDto.builder().name(FULL_NAME).active(true).build();
        Courier courier = Courier.builder().id(ID_1).name(FULL_NAME).active(true).build();

        given(courierService.updateCourier(ID_1, dto)).willReturn(courier);

        // when-then
        mockMvc.perform(put(COURIER_ID_PATH, ID_1)
                        .contentType(MediaType.APPLICATION_JSON).content(getJson(dto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("name", is(FULL_NAME)))
                .andExpect(jsonPath("active", is(true)));
    }

    @Test
    void givenInvalidId_whenUpdateCourier_thenThrowNotFoundException() throws Exception {
        // given
        String exception_message = "Courier with id 2 was not found";

        CourierDto dto = CourierDto.builder().name(FULL_NAME).active(true).build();

        given(courierService.updateCourier(ID_2, dto)).willThrow(new CourierNotFoundException(exception_message));

        // when-then
        MvcResult result = mockMvc.perform(put(COURIER_ID_PATH, ID_2)
                        .contentType(MediaType.APPLICATION_JSON).content(getJson(dto)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();

        String errorBody = result.getResponse().getContentAsString();
        assertThat(errorBody).isEqualTo(exception_message);
    }

    private static String getJson(CourierDto dto) {
        Gson gson = new Gson();
        return gson.toJson(dto);
    }
}