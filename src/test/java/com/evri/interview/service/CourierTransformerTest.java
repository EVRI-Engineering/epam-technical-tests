package com.evri.interview.service;

import com.evri.interview.model.Courier;
import com.evri.interview.repository.CourierEntity;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.evri.interview.utils.CourierUtils.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class CourierTransformerTest {
    private static CourierTransformer courierTransformer;

    @BeforeAll
    static void setup() {
        courierTransformer = new CourierTransformer();
    }

    @Test
    void shouldConvertEntityToDTO() {
        Courier expected = courier();

        Courier actual = courierTransformer.toCourier(courierEntity());

        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getName()).isEqualTo(expected.getName());
        assertThat(actual.isActive()).isEqualTo(expected.isActive());
    }

    @Test
    void shouldConvertDTOtoEntity() {
        CourierEntity expected = courierEntity();

        CourierEntity actual = courierTransformer.toCourierEntity(courier());

        assertActualAndExpectedEntities(actual, expected);
    }

    @Test
    void shouldThrowExceptionWithOneWordInName() {
        Courier dtoToConvert = courier();
        dtoToConvert.setName(COURIER_FIRST_NAME);
        CourierEntity expected = courierEntity();
        expected.setLastName("");

        assertThatThrownBy(() -> {
            courierTransformer.toCourierEntity(dtoToConvert);
        }).isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Name has been entered wrong");
    }

    private static void assertActualAndExpectedEntities(CourierEntity actual, CourierEntity expected) {
        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getFirstName()).isEqualTo(expected.getFirstName());
        assertThat(actual.getLastName()).isEqualTo(expected.getLastName());
        assertThat(actual.isActive()).isEqualTo(expected.isActive());
    }
}