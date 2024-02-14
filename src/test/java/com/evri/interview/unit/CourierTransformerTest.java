package com.evri.interview.unit;

import com.evri.interview.model.Courier;
import com.evri.interview.repository.CourierEntity;
import com.evri.interview.service.CourierTransformer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.evri.interview.CourierUtils.*;
import static org.assertj.core.api.Assertions.assertThat;

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
    void shouldConvertDTOWithNullNameToEntity() {
        Courier dtoToConvert = courier();
        dtoToConvert.setName(null);
        CourierEntity expected = courierEntity();
        expected.setFirstName("");
        expected.setLastName("");

        CourierEntity actual = courierTransformer.toCourierEntity(dtoToConvert);

        assertActualAndExpectedEntities(actual, expected);
    }

    @Test
    void shouldConvertDTOWithEmptyNameToEntity() {
        Courier dtoToConvert = courier();
        dtoToConvert.setName("");
        CourierEntity expected = courierEntity();
        expected.setFirstName("");
        expected.setLastName("");

        CourierEntity actual = courierTransformer.toCourierEntity(dtoToConvert);

        assertActualAndExpectedEntities(actual, expected);
    }

    @Test
    void shouldConvertDTOWithOneWordInNameToEntity() {
        Courier dtoToConvert = courier();
        dtoToConvert.setName(COURIER_FIRST_NAME);
        CourierEntity expected = courierEntity();
        expected.setLastName("");

        CourierEntity actual = courierTransformer.toCourierEntity(dtoToConvert);

        assertActualAndExpectedEntities(actual, expected);
    }

    @Test
    void shouldConvertDTOWithThreeWordsInNameToEntity() {
        Courier dtoToConvert = courier();
        dtoToConvert.setName(COURIER_FIRST_NAME + " " + COURIER_LAST_NAME + " " + SECOND_LAST_NAME);
        CourierEntity expected = courierEntity();
        expected.setLastName(COURIER_LAST_NAME + " " + SECOND_LAST_NAME);

        CourierEntity actual = courierTransformer.toCourierEntity(dtoToConvert);

        assertActualAndExpectedEntities(actual, expected);
    }

    private static void assertActualAndExpectedEntities(CourierEntity actual, CourierEntity expected) {
        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getFirstName()).isEqualTo(expected.getFirstName());
        assertThat(actual.getLastName()).isEqualTo(expected.getLastName());
        assertThat(actual.isActive()).isEqualTo(expected.isActive());
    }
}
