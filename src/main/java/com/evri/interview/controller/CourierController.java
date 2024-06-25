package com.evri.interview.controller;

import com.evri.interview.model.Courier;
import com.evri.interview.model.CourierDTO;
import com.evri.interview.service.CourierService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class CourierController {
    public static final String COURIER_NOT_FOUND_ERROR = "Courier not found";
    private CourierService courierService;

    @GetMapping("/couriers")
    public ResponseEntity<List<Courier>> getAllCouriers(@RequestParam(name = "isActive", required = false, defaultValue = "false") boolean isActive) {
        return ResponseEntity.ok(courierService.getAllCouriersWithActiveStatus(isActive));
    }

    @PutMapping("/couriers/{courierId}")
    public ResponseEntity<CourierDTO> updateCourier(@PathVariable long courierId, @RequestBody @Valid CourierDTO courier) {
        return courierService.updateCourier(courierId, courier)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, COURIER_NOT_FOUND_ERROR));
    }

}
