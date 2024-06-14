package com.evri.interview.controller;

import com.evri.interview.model.Courier;
import com.evri.interview.service.CourierService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class CourierController {

    private CourierService courierService;

    @GetMapping("/couriers")
    public ResponseEntity<List<Courier>> getAllCouriers(@RequestParam(required = false, defaultValue = "false") boolean isActive) {
        return ResponseEntity.ok(courierService.getCouriers(isActive));
    }

    @PutMapping("/couriers/{courierId}")
    public ResponseEntity<Courier> updateCourier(@PathVariable("courierId") Long courierId, @RequestBody @Valid Courier courier) {
        return ResponseEntity.ok(courierService.updateCourier(courierId, courier.getFirstName(), courier.getLastName(), courier.isActive())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }
}