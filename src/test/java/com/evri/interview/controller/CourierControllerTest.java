package com.evri.interview.controller;

import com.evri.interview.Application;
import com.evri.interview.model.Courier;
import com.evri.interview.service.CourierService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    private static final Courier COURIER_1 = Courier.builder()
            .id(1L)
            .name("John Doe")
            .active(true)
            .build();

    @Before
    public void setUp() {
        when(courierServiceMock.getAllCouriers(anyBoolean()))
                .thenReturn(List.of(COURIER_1));
    }

    @Test
    public void getAllCouriers_isActiveDefaultValue_returnsCouriersList() throws Exception {
        mvc.perform(get("/api/couriers"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[0].active").value(true));

        verify(courierServiceMock).getAllCouriers(false);
    }

    @Test
    public void getAllCouriers_isActiveTrueValue_returnsCouriersList() throws Exception {
        mvc.perform(get("/api/couriers")
                        .param("isActive", "true"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[0].active").value(true));

        verify(courierServiceMock).getAllCouriers(true);
    }
}
