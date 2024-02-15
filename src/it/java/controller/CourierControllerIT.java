package controller;

import com.evri.interview.Application;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
class CourierControllerIT {
    @Autowired
    MockMvc mockMvc;

    @Test
    void getAllCouriers() throws Exception {
        String expectedJson = "[{\"id\":1,\"name\":\"Ben Askew\",\"active\":true},{\"id\":2,\"name\":\"Tom Hardy\",\"active\":false}]";
        mockMvc.perform(get("/api/couriers"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(expectedJson));
    }

    @Test
    void getActiveCouriers() throws Exception {
        String expectedJson = "[{\"id\":1,\"name\":\"Ben Askew\",\"active\":true}]";
        mockMvc.perform(get("/api/couriers").queryParam("isActive", "true"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(expectedJson));
    }

    @Test
    void putCourier_notFound() throws Exception {
        mockMvc.perform(put("/api/couriers/3")
                        .content("{\"id\":3,\"name\":\"Ben Askew\",\"active\":true}")
                        .contentType("application/json"))
                .andExpect(status().isNotFound());
    }

    @Test
    void putCourier_shouldFailWhenWrongName() throws Exception {
        mockMvc.perform(put("/api/couriers/1")
                        .content("{\"id\":1,\"name\":\"Ben\",\"active\":true}")
                        .contentType("application/json"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void putCourier() throws Exception {
        mockMvc.perform(put("/api/couriers/1")
                        .content("{\"id\":1,\"name\":\"Max Liskov\",\"active\":true}")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1,\"name\":\"Max Liskov\",\"active\":true}"));
    }
}