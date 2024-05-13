package com.evri.interview.service;

import com.evri.interview.Application;
import com.evri.interview.model.Courier;
import com.evri.interview.repository.CourierEntity;
import com.evri.interview.repository.CourierRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

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

    private static final CourierEntity COURIER_ENTITY_1 = CourierEntity.builder()
            .id(1L)
            .firstName("John")
            .lastName("Doe")
            .active(true)
            .build();
    private static final Courier COURIER_1 = Courier.builder()
            .id(1L)
            .name("John Doe")
            .active(true)
            .build();

    @Before
    public void setUp() {
        Mockito.when(courierRepositoryMock.findAll())
                .thenReturn(List.of(COURIER_ENTITY_1));
        Mockito.when(courierRepositoryMock.save(any()))
                .thenReturn(COURIER_ENTITY_1);
    }

    @Test
    public void getAllCouriers_returnCouriersList() {
        List<Courier> expected = List.of(COURIER_1);

        List<Courier> actual = courierService.getAllCouriers();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void updateCourier_courierFound_returnUpdatedCourier() {
        Mockito.when(courierRepositoryMock.existsById(any()))
                .thenReturn(true);

        Courier expected = COURIER_1;

        Optional<Courier> actual = courierService.updateCourier(COURIER_1.getId(), COURIER_1);

        assertThat(actual.isPresent()).isTrue();
        assertThat(actual.get()).isEqualTo(expected);
    }

    @Test
    public void updateCourier_courierNotFound_returnOptionalEmpty() {
        Mockito.when(courierRepositoryMock.existsById(any()))
                .thenReturn(false);

        Optional<Courier> actual = courierService.updateCourier(COURIER_1.getId(), COURIER_1);

        assertThat(actual.isPresent()).isFalse();
    }
}
