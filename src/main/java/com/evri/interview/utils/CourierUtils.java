package com.evri.interview.utils;

import com.evri.interview.dto.CourierNameContext;
import java.util.Optional;

public final class CourierUtils {

    private static final String NAME_SEPARATOR = " ";
    private static final int NAME_LENGTH = 2;
    private static final int FIRST_NAME_INDEX = 0;
    private static final int LAST_NAME_INDEX = 1;

    private CourierUtils() {
    }

    public static CourierNameContext getNameContext(final String fullName) {
        return Optional.ofNullable(fullName)
                .map(name -> mapToContext(fullName))
                .orElse(CourierNameContext.builder().build());
    }

    //need more input of which value could full name be
    private static CourierNameContext mapToContext(String fullName) {
        String[] names = fullName.split(NAME_SEPARATOR);
        if (names.length != NAME_LENGTH) {
            throw new IllegalArgumentException("Invalid fullName format: " + fullName);
        }
        return CourierNameContext.builder()
                .firstName(names[FIRST_NAME_INDEX])
                .lastName(names[LAST_NAME_INDEX])
                .build();
    }
}
