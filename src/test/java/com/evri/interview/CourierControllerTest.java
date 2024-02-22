package com.evri.interview;

import com.evri.interview.dto.CourierDto;
import com.evri.interview.exception.UpdateNonExistantCourierException;
import com.evri.interview.model.Courier;
import com.evri.interview.service.CourierService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.evri.interview.TestDataProvider.getCourierDtoAsString;
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
@EnableAutoConfiguration
public class CourierControllerTest {

    @MockBean
    CourierService courierService;
    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("Find all inactive couriers")
    void findAllInactiveCouriers() throws Exception {
        when(courierService.getAllActiveCouriers(false)).thenReturn(
                List.of(Courier.builder().id(3).name("Courier Inactive").active(false).build()));
        mockMvc.perform(get("/api/couriers")
                        .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(3))
                .andExpect(jsonPath("$[0].name").value("Courier Inactive"))
                .andExpect(jsonPath("$[0].active").value(false));
    }

    @Test
    @DisplayName("Find all active couriers")
    void findAllActiveCouriers() throws Exception {
        when(courierService.getAllActiveCouriers(true)).thenReturn(
                List.of(Courier.builder().id(1).name("Courier Active").active(true).build()));
        mockMvc.perform(get("/api/couriers?isActive=true")
                        .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Courier Active"))
                .andExpect(jsonPath("$[0].active").value(true));
    }

    @Test
    @DisplayName("Update non existent courier and throw Not Found 404")
    void updateUnknownEntity() throws Exception {
        doThrow(new UpdateNonExistantCourierException("Attempt of update non existent courier")).when(courierService).updateCourier(anyLong(), any(CourierDto.class));

        mockMvc.perform(put("/api/couriers/{courierId}", 2)
                        .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
                        .content(getCourierDtoAsString("New","Courier", true)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Update existent courier, status:200")
    void updateKnownEntity() throws Exception {
        mockMvc.perform(put("/api/couriers/{courierId}", 3)
                .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
                .content(getCourierDtoAsString("Courier", "Existent", false)))
                .andExpect(status().isOk());
    }

}
