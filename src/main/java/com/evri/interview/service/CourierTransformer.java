package com.evri.interview.service;

import com.evri.interview.model.Courier;
import com.evri.interview.repository.CourierEntity;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
public class CourierTransformer {

    public Courier toCourier(CourierEntity entity) {
        return Courier.builder()
                .id(entity.getId())
                .name(String.format("%s %s", entity.getFirstName(), entity.getLastName()))
                .active(entity.isActive())
                .build();
    }

    public CourierEntity toCourierEntity(Courier courier) {
        FullName fullname = parseFullName(courier);
        return CourierEntity.builder()
                .id(courier.getId())
                .firstName(fullname.getFirstName())
                .lastName(fullname.getLastName())
                .active(courier.isActive())
                .build();
    }

    private FullName parseFullName(Courier courier) {
        if (courier.getName() == null) {
            return new FullName();
        }
        String[] splitName = courier.getName().split("\\s+");
        FullName fullName = new FullName();
        fullName.setFirstName(splitName[0]);
        if (splitName.length > 1) {
            // for 2 or more words in the 'name' field of Courier
            // treat the first word as a firstName and all other words as a part of lastName
            int idx = splitName[0].length() + 1;
            fullName.setLastName(courier.getName().substring(idx));
        }
        return fullName;
    }

    @Data
    private static class FullName {
        private String firstName;
        private String lastName;

        public FullName() {
            firstName = lastName = "";
        }
    }
}
