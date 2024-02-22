package com.evri.interview;

import com.evri.interview.dto.CourierDto;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TestDataProvider {
    public static CourierDto getCourierDto(String name, String lastname, boolean isActive) {
        return CourierDto.builder().name(name).lastName(lastname).isActive(isActive).build();
    }

    public static String getCourierDtoAsString(String name, String lastname, boolean isActive) throws Exception {
        return new ObjectMapper().writeValueAsString(getCourierDto(name, lastname, isActive));
    }
}
