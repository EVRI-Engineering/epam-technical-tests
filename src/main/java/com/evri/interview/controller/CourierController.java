package com.evri.interview.controller;

import com.evri.interview.model.Courier;
import com.evri.interview.service.CourierService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class CourierController {

    private CourierService courierService;

    @GetMapping("/couriers")
    public ResponseEntity<List<Courier>> getAllCouriers() {

        return ResponseEntity.ok(courierService.getAllCouriers());
    }

    @PutMapping("/couriers/{id}")
    public ResponseEntity<Courier> updateCourier(@PathVariable final Long id,
                                                 @RequestBody Courier courier) {

        Optional<Courier> optionalCourier = courierService.updateCourier(id, courier);
        return optionalCourier.map(ResponseEntity::ok).orElseGet(() ->
                new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
