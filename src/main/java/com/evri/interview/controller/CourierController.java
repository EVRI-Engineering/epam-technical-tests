package com.evri.interview.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.evri.interview.model.Courier;
import com.evri.interview.model.CourierUpdateDto;
import com.evri.interview.service.CourierService;
import com.evri.interview.service.CourierTransformer;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class CourierController {

    private CourierService courierService;
    private CourierTransformer courierTransformer;

    @GetMapping("/couriers")
    public ResponseEntity<List<Courier>> getAllCouriers(@RequestParam(defaultValue = "false") boolean isActive) {
        if (isActive) {
            return ResponseEntity.ok(courierService.getAllActiveCouriers());
        }
        return ResponseEntity.ok(courierService.getAllCouriers());
    }

    @PutMapping("/couriers/{courierId}")
    public ResponseEntity<Courier> updateCourier(
      @PathVariable final long courierId,
      @RequestBody final CourierUpdateDto courierDto
    ) {
        log.debug("Process courier update request with body: {} and courierId: {}", courierDto, courierId);
        final Optional<Courier> updatedCourier = courierService.updateCourier(courierId, courierDto);

        return updatedCourier.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
