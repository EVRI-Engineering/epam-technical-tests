package com.evri.interview.controller;

import com.evri.interview.model.Courier;
import com.evri.interview.service.CourierService;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class CourierController {

    private CourierService courierService;

    @GetMapping("/couriers")
    public ResponseEntity<List<Courier>> getAllCouriers(
        @RequestParam(name = "isActive", defaultValue = "false") boolean isActive) {

        return ResponseEntity.ok(courierService.getAllCouriers(isActive));
    }

    @PutMapping("/couriers/{courierId}")
    public ResponseEntity<Courier> updateCourier(@PathVariable long courierId,
        @RequestBody Courier courierDetails) {
        Optional<Courier> updatedCourier = courierService.updateCourier(courierId, courierDetails);

        return updatedCourier
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
