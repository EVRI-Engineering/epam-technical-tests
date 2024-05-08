package com.evri.interview.controller;

import com.evri.interview.model.Courier;
import com.evri.interview.model.UpdateCourierRequest;
import com.evri.interview.service.CourierService;
import lombok.AllArgsConstructor;
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
@RequestMapping("/api/couriers")
public class CourierController {

    private CourierService courierService;

    @GetMapping
    public ResponseEntity<List<Courier>> getAllCouriers(@RequestParam(required = false) boolean isActive) {
        List<Courier> couriers = isActive ? courierService.getAllActiveCouriers() : courierService.getAllCouriers();

        return ResponseEntity.ok(couriers);
    }

    @PutMapping("/{courierId}")
    public ResponseEntity<Courier> updateCourier(@PathVariable long courierId, @RequestBody UpdateCourierRequest updateCourierRequest) {
        return ResponseEntity.ok(courierService.updateCourier(courierId, updateCourierRequest));
    }
}
