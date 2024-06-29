package com.evri.interview.service;

import com.evri.interview.controller.dto.request.CourierDto;
import com.evri.interview.exception.CourierNotFoundException;
import com.evri.interview.mapper.CourierMapper;
import com.evri.interview.model.Courier;
import com.evri.interview.repository.CourierEntity;
import com.evri.interview.repository.CourierRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class CourierService {

	private CourierRepository repository;
	private CourierMapper courierMapper;

	public List<Courier> getCouriers(boolean filterByIsActive) {
		return filterByIsActive ? getActiveCouriers() : getAllCouriers();
	}

	@Transactional
	public Courier updateCourierIfExists(long id, CourierDto dto) {
		return repository.findById(id)
			.map(courier -> {
				CourierEntity courierEntity = courierMapper.dtoToEntity(dto, id);
				return courierMapper.entityToModel(repository.saveAndFlush(courierEntity));
			})
			.orElseThrow(() -> new CourierNotFoundException(id));
	}

	private List<Courier> getActiveCouriers() {
		return repository.getAllByActiveIsTrue()
			.stream()
			.map(courierMapper::entityToModel)
			.collect(Collectors.toList());
	}

	private List<Courier> getAllCouriers() {
		return repository.findAll()
			.stream()
			.map(courierMapper::entityToModel)
			.collect(Collectors.toList());
	}
}
