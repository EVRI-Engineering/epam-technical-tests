package com.evri.interview.converter;

import com.evri.interview.model.Courier;
import com.evri.interview.model.CourierForUpdate;
import com.evri.interview.repository.CourierEntity;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CourierConverter {

    CourierConverter INSTANCE = Mappers.getMapper(CourierConverter.class);

    @Mapping(target = "name", expression = "java(String.format(\"%s %s\", entity.getFirstName(), entity.getLastName()))")
    Courier entityToCourier(CourierEntity entity);

    CourierEntity updateEntity(@MappingTarget CourierEntity entity, CourierForUpdate courier);

}
