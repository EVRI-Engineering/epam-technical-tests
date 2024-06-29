package com.evri.interview.mapper;

import com.evri.interview.controller.dto.request.CourierDto;
import com.evri.interview.model.Courier;
import com.evri.interview.repository.CourierEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CourierMapper {

	@Mapping(target = "name", expression = "java(dto.getFirstName() + ' ' + dto.getLastName())")
	Courier entityToModel(CourierEntity dto);

	CourierEntity dtoToEntity(CourierDto dto, long id);

}
