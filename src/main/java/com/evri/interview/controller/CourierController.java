package com.evri.interview.controller;

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
@RequestMapping("/api/couriers")
public class CourierController {

    private CourierService courierService;

    @GetMapping
    public ResponseEntity<List<Courier>> getAllCouriers(
            @RequestParam(required = false, defaultValue = "false", name = "isActive") boolean fetchActive) {

        return ResponseEntity.ok(courierService.getCouriers(fetchActive));
    }

    @PutMapping("/{courierId}")
    public ResponseEntity<?> updateCourier(@RequestBody Courier courier,
                                           @PathVariable Long courierId) {

        courierService.updateCourierDetails(courier, courierId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
