package com.evri.interview.controller;

import com.evri.interview.Application;
import com.evri.interview.model.UpdateCourierRequest;
import com.evri.interview.repository.CourierEntity;
import com.evri.interview.repository.CourierRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.MOCK,
    classes = Application.class)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class CourierControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private CourierRepository courierRepository;

    @Test
    public void getAllCouriers() throws Exception {
        mvc.perform(get("/api/couriers")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content()
                .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].name", is("Ben Askew")))
            .andExpect(jsonPath("$[0].active", is(true)))
            .andExpect(jsonPath("$[1].name", is("David Gilmour")))
            .andExpect(jsonPath("$[1].active", is(false)));
    }

    @Test
    public void getOnlyActiveCouriers() throws Exception {
        mvc.perform(get("/api/couriers")
                .contentType(MediaType.APPLICATION_JSON)
                .queryParam("isActive", "true"))
            .andExpect(status().isOk())
            .andExpect(content()
                .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].name", is("Ben Askew")))
            .andExpect(jsonPath("$[0].active", is(true)))
            .andExpect(jsonPath("$", hasSize(equalTo(1))));
    }

    @Test
    public void updateCourierName() throws Exception {
        mvc.perform(put("/api/couriers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(UpdateCourierRequest.builder().name("Jack Askew").active(true).build())))
            .andExpect(status().isOk())
            .andExpect(content()
                .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("name", is("Jack Askew")))
            .andExpect(jsonPath("active", is(true)));

        final CourierEntity courier = courierRepository.findById(1L).get();
        assertThat(courier).extracting(CourierEntity::getFirstName).isEqualTo("Jack");
    }

    @Test
    public void updateCourierNameInvalidRequest() throws Exception {
        mvc.perform(put("/api/couriers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(UpdateCourierRequest.builder().name("JackAskew").active(true).build())))
            .andExpect(status().isBadRequest())
            .andExpect(content()
                .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("name", is("Name must contain two words, splitted by space")));
    }

    @Test
    public void updateCourierNamenotFound() throws Exception {
        mvc.perform(put("/api/couriers/5")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(UpdateCourierRequest.builder().name("Jack Askew").active(true).build())))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("message", is("Courier with id = 5 not found")));
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
