package com.evri.interview.controller;

import com.evri.interview.controller.dto.request.CourierDto;
import com.evri.interview.model.Courier;
import com.evri.interview.service.CourierService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/couriers")
public class CourierController {

	private CourierService courierService;

	@GetMapping
	public ResponseEntity<List<Courier>> getAllCouriers(
		@RequestParam(required = false, defaultValue = "false") boolean isActive) {
		return ResponseEntity.ok(courierService.getCouriers(isActive));
	}

	@PutMapping("/{id}")
	public ResponseEntity<Courier> updateCourierIfExists(@PathVariable long id,
		@RequestBody @Validated CourierDto courierDto) {
		return ResponseEntity.ok(courierService.updateCourierIfExists(id, courierDto));
	}

}
