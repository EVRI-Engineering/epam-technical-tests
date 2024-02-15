package com.evri.interview.utils;

import com.evri.interview.dto.CourierNameContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class CourierUtilsTest {

    @Test
    @DisplayName("Get name context should get")
    void getNameContext_shouldGetTest() {
        CourierNameContext nameContext = CourierUtils.getNameContext("Andriy Okhrymovych");
        assertThat(nameContext.getFirstName()).isEqualTo("Andriy");
        assertThat(nameContext.getLastName()).isEqualTo("Okhrymovych");
    }

    @Test
    @DisplayName("Get name context should throw exception")
    void getNameContext_shouldThrowExceptionTest() {
        assertThatThrownBy(() -> CourierUtils.getNameContext("AndriyOkhrymovych"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid fullName format: AndriyOkhrymovych");
    }

    @Test
    @DisplayName("Get name context null should return empty context")
    void getNameContextNull_shouldReturnEmptyContextTest() {
        CourierNameContext nameContext = CourierUtils.getNameContext(null);
        assertThat(nameContext).isEqualTo(CourierNameContext.builder().build());
    }
}