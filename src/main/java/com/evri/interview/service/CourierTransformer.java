package com.evri.interview.service;

import com.evri.interview.exception.InvalidNameFormatException;
import com.evri.interview.model.Courier;
import com.evri.interview.entity.CourierEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
public class CourierTransformer {

    private static final String NAME_DELIMITER_REGEXP = "\\s+";

    public Courier toCourier(CourierEntity entity) {
        return Courier.builder()
                .id(entity.getId())
                .name(String.format("%s %s", entity.getFirstName(), entity.getLastName()))
                .active(entity.isActive())
                .build();
    }

    public CourierEntity toUpdatedCourierEntity(CourierEntity oldCourier,
                                                Courier newCourier) {
        updateFullName(oldCourier, newCourier);
        updateActive(oldCourier, newCourier);

        return oldCourier;
    }

    private static void updateActive(CourierEntity oldCourier, Courier newCourier) {
        if(Objects.nonNull(newCourier.getActive())) {
            oldCourier.setActive(newCourier.getActive());
        }
    }

    private void updateFullName(CourierEntity oldCourier, Courier newCourier) {
        if(!StringUtils.isBlank(newCourier.getName())) {
            String[] fullName = Optional.ofNullable(newCourier.getName())
                    .map(String::trim)
                    .map(name -> name.split(NAME_DELIMITER_REGEXP))
                    .orElse(new String[0]);
            if(fullName.length == 2) {
                oldCourier.setFirstName(fullName[0]);
                oldCourier.setLastName(fullName[1]);
            }
            else {
                throw getInvalidNameException(newCourier.getName());
            }
        }
    }

    private InvalidNameFormatException getInvalidNameException(String name) {
        return new InvalidNameFormatException(
                String.format("Invalid format was used in provided name parameter: %s", name));
    }

}
