package com.evri.interview.controller;

import com.evri.interview.model.Courier;
import com.evri.interview.service.CourierService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/couriers")
public class CourierController {

    private CourierService courierService;

    @GetMapping
    public ResponseEntity<List<Courier>> getAllCouriers(
            @RequestParam(required = false, defaultValue = "false") boolean isActive) {
        log.info("Call method getAllCouriers() isActive: {}", isActive);
        return ResponseEntity.ok(courierService.getAllCouriers(isActive));
    }

    @PutMapping("/{courierId}")
    public ResponseEntity<Void> updateCourier(@PathVariable final long courierId,
                                              @RequestBody final Courier courierRequest) {
        log.info("Before updateCourier method call. CourierId: {}", courierId);
        courierService.updateCourier(courierId, courierRequest);
        log.info("After updateCourier method call. CourierId: {}", courierId);
        return ResponseEntity.noContent().build();
    }
}
