package com.evri.interview.controller;

import com.evri.interview.controller.dto.request.CourierDto;
import com.evri.interview.model.Courier;
import com.evri.interview.service.CourierService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1")
public class CourierController {

	private CourierService courierService;

	@GetMapping("/couriers")
	public ResponseEntity<List<Courier>> getAllCouriers(
		@RequestParam(required = false, defaultValue = "false", name = "isActive") boolean filterByIsActive) {
		return ResponseEntity.ok(courierService.getCouriers(filterByIsActive));
	}

	@PutMapping("/couriers/{id}")
	public ResponseEntity<Courier> updateCourierIfExists(@PathVariable long id, @RequestBody CourierDto courierDto) {
		return ResponseEntity.ok(courierService.updateCourierIfExists(id, courierDto));
	}

}
