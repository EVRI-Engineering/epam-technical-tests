package com.evri.interview.controller;

import com.evri.interview.model.Courier;
import com.evri.interview.service.CourierService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class CourierController {

    private CourierService courierService;

    @GetMapping("/couriers")
    public ResponseEntity<List<Courier>> getCouriers(@RequestParam(required = false) Boolean isActive) {
        return ResponseEntity.ok(courierService.getCouriers(isActive));
    }

    @PutMapping("/{courierId}")
    public ResponseEntity<Courier> updateCourier(@PathVariable Long courierId, @RequestBody Courier updatedCourier) {
        if (!Objects.equals(courierId, updatedCourier.getId())) {
            throw new IllegalStateException("Invalid courier id");
        }
        Courier updated = courierService.updateCourier(courierId, updatedCourier);
        return ResponseEntity.ok(updated);
    }
}
