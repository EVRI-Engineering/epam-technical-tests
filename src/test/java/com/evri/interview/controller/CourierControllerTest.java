package com.evri.interview.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import com.evri.interview.config.GlobalExceptionHandler;
import com.evri.interview.dto.CourierRequest;
import com.evri.interview.dto.CourierResponse;
import com.evri.interview.exception.ResourceNotFoundException;
import com.evri.interview.service.ICourierService;
import com.evri.interview.util.DataProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(SpringExtension.class)
class CourierControllerTest {
    public static final String URL = "/api/couriers";
    public static final String URL_WITH_PATH_VARIABLE = URL + "/{courierId}";
    public static final String ERROR_MESSAGE = "The data is filled incorrectly.";


    @Mock
    private ICourierService courierService;
    private CourierController courierController;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void init() {
        courierController = new CourierController(courierService);
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders
                .standaloneSetup(courierController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @SneakyThrows
    public void getAllCouriersIfIsActiveNotPassedShouldReturnResponseAndStatusOkTest() {
        //Given
        List<CourierResponse> couriers = Lists.list(
                DataProvider.buildCourierResponse(1, "Robert", true),
                DataProvider.buildCourierResponse(2, "Kris", false));
        when(courierService.getAllCouriers(false)).thenReturn(couriers);

        //When
        ResultActions perform = mockMvc.perform(get(URL));
        //Then
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.content").exists())
                .andExpect(jsonPath("$.errors").doesNotExist())
                .andExpect(jsonPath("$.content[0].id", is(1)))
                .andExpect(jsonPath("$.content[0].name", is("Robert")))
                .andExpect(jsonPath("$.content[0].active", is(true)))
                .andExpect(jsonPath("$.content[1].id", is(2)))
                .andExpect(jsonPath("$.content[1].name", is("Kris")))
                .andExpect(jsonPath("$.content[1].active", is(false)));

        verify(courierService, only()).getAllCouriers(false);
    }

    @Test
    @SneakyThrows
    public void getAllCouriersIfIsActiveIsPassedShouldReturnResponseAndStatusOkTest() {
        //Given
        List<CourierResponse> couriers = Lists.list(DataProvider.buildCourierResponse(1, "Robert", true));
        when(courierService.getAllCouriers(true)).thenReturn(couriers);

        //When
        ResultActions perform = mockMvc.perform(get(URL).param("isActive", "true"));
        //Then
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.content").exists())
                .andExpect(jsonPath("$.errors").doesNotExist())
                .andExpect(jsonPath("$.content[0].id", is(1)))
                .andExpect(jsonPath("$.content[0].name", is("Robert")))
                .andExpect(jsonPath("$.content[0].active", is(true)));

        verify(courierService, only()).getAllCouriers(true);
    }

    @Test
    @SneakyThrows
    public void getAllCouriersIfIsActivePassedIncorrectShouldReturnErrorResponseAndBadRequestStatusTest() {
        //Given

        //When
        ResultActions perform = mockMvc.perform(get(URL).param("isActive", "test"));
        //Then
        perform.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.content").doesNotExist())
                .andExpect(jsonPath("$.errors").exists())
                .andExpect(jsonPath("$.errors.status", is(HttpStatus.BAD_REQUEST.name())))
                .andExpect(jsonPath("$.errors.message").exists())
                .andExpect(jsonPath("$.errors.currentDatetime").exists());

        verify(courierService, never()).getAllCouriers(anyBoolean());
    }

    @Test
    @SneakyThrows
    public void updateCourierWithCorrectDataShouldUpdateOneAndReturnResponseAndOkStatusTest() {
        //Given
        CourierRequest courierRequest = DataProvider.buildCourierRequest("John", "Nolan", true);
        CourierResponse courierResponse = DataProvider.buildCourierResponse(1, "John Nolan", true);
        when(courierService.updateCourier(1, courierRequest)).thenReturn(courierResponse);
        //When
        ResultActions perform = mockMvc.perform(put(URL_WITH_PATH_VARIABLE, 1)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(courierRequest)));
        //Then
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.content").exists())
                .andExpect(jsonPath("$.errors").doesNotExist())
                .andExpect(jsonPath("$.content.id", is(1)))
                .andExpect(jsonPath("$.content.name", is("John Nolan")))
                .andExpect(jsonPath("$.content.active", is(true)));
        verify(courierService, only()).updateCourier(1, courierRequest);
    }

    @Test
    @SneakyThrows
    public void updateCourierWithNonExistingCourierShouldReturnErrorAndNotFoundStatusTest() {
        //Given
        CourierRequest courierRequest = DataProvider.buildCourierRequest("John", "Nolan", true);
        when(courierService.updateCourier(1, courierRequest))
                .thenThrow(new ResourceNotFoundException("Courier not found. courierId = 1"));
        //When
        ResultActions perform = mockMvc.perform(put(URL_WITH_PATH_VARIABLE, 1)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(courierRequest)));
        //Then
        perform.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.content").doesNotExist())
                .andExpect(jsonPath("$.errors").exists())
                .andExpect(jsonPath("$.errors.status", is(HttpStatus.NOT_FOUND.name())))
                .andExpect(jsonPath("$.errors.message", is("Courier not found. courierId = 1")))
                .andExpect(jsonPath("$.errors.currentDatetime").exists());
        verify(courierService, only()).updateCourier(1, courierRequest);
    }

    @Test
    @SneakyThrows
    public void updateCourierWithEmptyBodyShouldReturnErrorAndBadRequestStatusTest() {
        //Given
        CourierRequest courierRequest = DataProvider.buildCourierRequest("", "", null);
        //When
        ResultActions perform = mockMvc.perform(put(URL_WITH_PATH_VARIABLE, 1)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(courierRequest)));
        //Then
       performBadRequest(perform)
               .andExpect(jsonPath("$.errors.additionalInfo.firstName", is("First name should not be blank.")))
               .andExpect(jsonPath("$.errors.additionalInfo.lastName", is("Last name should not be blank.")))
               .andExpect(jsonPath("$.errors.additionalInfo.active", is("Active status must be provided.")));
        verify(courierService, never()).updateCourier(anyLong(), any(CourierRequest.class));
    }


    @Test
    @SneakyThrows
    public void updateCourierWithLongFieldsShouldReturnErrorAndBadRequestStatusTest() {
        //Given
        CourierRequest courierRequest = DataProvider.buildCourierRequest(
                "FirstNameJohn123456789012345678901234567890123456789012345678901234567890", "Nolan", true);
        //When
        ResultActions perform = mockMvc.perform(put(URL_WITH_PATH_VARIABLE, 1)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(courierRequest)));
        //Then
        performBadRequest(perform)
                .andExpect(jsonPath("$.errors.additionalInfo.firstName",
                        is("First name should be less then 64 characters.")));
        verify(courierService, never()).updateCourier(anyLong(), any(CourierRequest.class));
    }

    private ResultActions performBadRequest(ResultActions perform) throws Exception {
        return perform.andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.errors").exists())
                .andExpect(jsonPath("$.errors.status", is(HttpStatus.BAD_REQUEST.name())))
                .andExpect(jsonPath("$.errors.message", is(ERROR_MESSAGE)))
                .andExpect(jsonPath("$.errors.currentDatetime").exists())
                .andExpect(jsonPath("$.errors.additionalInfo").exists());
    }
}