package com.evri.interview.controller;

import com.evri.interview.model.Courier;
import com.evri.interview.model.CourierDTO;
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
@RequestMapping("/api")
public class CourierController {

    private CourierService courierService;

    @GetMapping("/couriers")
    public ResponseEntity<List<Courier>> getAllCouriers(@RequestParam(required = false, defaultValue = "false") boolean isActive) {
        return isActive ?
            ResponseEntity.ok(courierService.getAllActiveCouriers()) :
            ResponseEntity.ok(courierService.getAllCouriers());
    }

    @PutMapping("/couriers/{courierId}")
    public ResponseEntity<Courier> updateCourierById(@PathVariable long courierId, @RequestBody CourierDTO courierToUpdate) {
        try {
            Courier courier = courierService.updateCourierById(courierId, courierToUpdate);
            return ResponseEntity.ok().body(courier);
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.notFound().build();
        }
    }

}
