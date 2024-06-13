package com.evri.interview.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.evri.interview.model.Courier;
import com.evri.interview.service.CourierService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class CourierController {

    private CourierService courierService;

    @GetMapping("/couriers")
    public ResponseEntity<List<Courier>> getAllCouriers() {

        return ResponseEntity.ok(courierService.getAllCouriers());
    }

    @PutMapping("/couriers/{courierId}")
    public ResponseEntity<Courier> updateCourier(
      @PathVariable final long courierId,
      @RequestBody final Courier courierDto
    ) {
        log.debug("Process incoming request with body: {} and courierId: {}", courierDto, courierId);
        final Optional<Courier> courier = courierService.getCourierByID(courierId);
        if (courier.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        if (courierDto.getId() != courierId) {
            throw new IllegalArgumentException("ID in path variable and ID in request body do not match");
        }
        Courier updatedCourier = courierService.saveCourier(courierDto);
        return ResponseEntity.ok(updatedCourier);
    }
}
