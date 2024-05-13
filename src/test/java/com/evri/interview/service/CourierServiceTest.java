package com.evri.interview.service;

import com.evri.interview.Application;
import com.evri.interview.model.Courier;
import com.evri.interview.repository.CourierEntity;
import com.evri.interview.repository.CourierRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = Application.class)
@AutoConfigureMockMvc
public class CourierServiceTest {
    @MockBean
    private CourierRepository courierRepositoryMock;
    @Autowired
    private CourierService courierService;

    private static final Courier COURIER_ACTIVE = Courier.builder()
            .id(1L)
            .name("John Doe")
            .active(true)
            .build();
    private static final Courier COURIER_INACTIVE = Courier.builder()
            .id(1L)
            .name("Irisu Fuyumi")
            .active(false)
            .build();
    private static final CourierEntity COURIER_ENTITY_ACTIVE = CourierEntity.builder()
            .id(1L)
            .firstName("John")
            .lastName("Doe")
            .active(true)
            .build();
    private static final CourierEntity COURIER_ENTITY_INACTIVE = CourierEntity.builder()
            .id(1L)
            .firstName("Irisu")
            .lastName("Fuyumi")
            .active(false)
            .build();
    private static final boolean IS_ONLY_ACTIVE_TRUE = true;
    private static final boolean IS_ONLY_ACTIVE_FALSE= false;

    @Before
    public void setUp() {
        when(courierRepositoryMock.findAll())
                .thenReturn(List.of(COURIER_ENTITY_ACTIVE, COURIER_ENTITY_INACTIVE));
    }

    @Test
    public void getAllCouriers_onlyActiveCouriers_returnCouriersList() {
        List<Courier> expected = List.of(COURIER_ACTIVE);

        List<Courier> actual = courierService.getAllCouriers(IS_ONLY_ACTIVE_TRUE);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void getAllCouriers_allCouriers_returnCouriersList() {
        List<Courier> expected = List.of(COURIER_ACTIVE, COURIER_INACTIVE);

        List<Courier> actual = courierService.getAllCouriers(IS_ONLY_ACTIVE_FALSE);

        assertThat(actual).isEqualTo(expected);
    }
}
