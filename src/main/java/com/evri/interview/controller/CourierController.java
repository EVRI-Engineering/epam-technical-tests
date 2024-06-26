package com.evri.interview.controller;

import com.evri.interview.exception.CourierNotFoundException;
import com.evri.interview.model.Courier;
import com.evri.interview.service.CourierService;
import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class CourierController {

    private CourierService courierService;

    @GetMapping("/couriers")
    public ResponseEntity<List<Courier>> getAllCouriers(@RequestParam(value = "isActive", defaultValue = "false") boolean isActive) {
        if(isActive) {
            return ResponseEntity.ok(courierService.getActiveCouriers());
        } else {
            return ResponseEntity.ok(courierService.getAllCouriers());
        }
    }

    @PutMapping("/couriers/{courierId}")
    public ResponseEntity<?> updateCourier(@PathVariable(value = "courierId") long courierId, @RequestBody Courier courierDetails) {
        try {
            Courier updatedCourier = courierService.updateCourierById(courierId, courierDetails);
            return new ResponseEntity<>(updatedCourier, HttpStatus.OK);
        } catch (CourierNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
