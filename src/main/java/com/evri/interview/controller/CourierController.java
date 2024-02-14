package com.evri.interview.controller;

import com.evri.interview.model.Courier;
import com.evri.interview.model.CourierUpdatedResponse;
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
    public ResponseEntity<List<Courier>> getAllCouriers(
        @RequestParam(required = false, defaultValue = "false") Boolean isActive) {

        return ResponseEntity.ok(courierService.getAllCouriersOrActive(isActive));
    }

    @PutMapping("/couriers/{courierId}")
    public ResponseEntity<Object> updateCouriers(
        @PathVariable long courierId, @RequestBody Courier courier) {
        courierService.updateCourierById(courier, courierId);
        return new ResponseEntity<>(
            new CourierUpdatedResponse(
                String.format("Courier was successfully updated. Courier id: %d", courierId)),
            HttpStatus.OK);
    }
}
