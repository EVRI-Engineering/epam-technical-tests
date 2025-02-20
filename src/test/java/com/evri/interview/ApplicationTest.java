package com.evri.interview;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ApplicationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetAllCouriers() throws Exception {
        boolean isActive = false;

        mockMvc.perform(get("/api/couriers")
                .param("isActive", String.valueOf(isActive)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].name")
                .value("John Doe"))
            .andExpect(jsonPath("$[1].name")
                .value("Jane Smith"));
    }

    @Test
    void testGetActiveCouriers() throws Exception {
        boolean isActive = true;

        mockMvc.perform(get("/api/couriers")
                .param("isActive", String.valueOf(isActive)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$[0].name")
                .value("Jane Smith"));
    }

    @Test
    void testUpdateCourier() throws Exception {
        long courierId = 1L;

        mockMvc.perform(put("/api/couriers/{courierId}", courierId)
                .contentType("application/json")
                .content("{\"firstName\":\"NewName\",\"lastName\":\"NewLastName\",\"active\":true}"))
            .andExpect(status().isOk())
            .andExpect(content().json("{'id':1,'name':'NewName NewLastName','active':true}"));
    }

    @Test
    void testUpdateCourier_whenCourierNotFound() throws Exception {
        long courierId = 5L;

        mockMvc.perform(put("/api/couriers/{courierId}", courierId)
                .contentType("application/json")
                .content("{\"firstName\":\"NewName\",\"lastName\":\"NewLastName\",\"active\":true}"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message").value(String.format("Courier with id %d not found", courierId)));
    }
}
