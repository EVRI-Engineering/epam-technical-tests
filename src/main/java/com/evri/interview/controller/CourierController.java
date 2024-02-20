package com.evri.interview.controller;

import com.evri.interview.model.Courier;
import com.evri.interview.model.CourierForUpdate;
import com.evri.interview.service.CourierService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class CourierController {

    private CourierService courierService;

    @GetMapping("/couriers")
    public ResponseEntity<List<Courier>> getAllCouriers(@RequestParam(defaultValue = "false") boolean isActive) {
        return ResponseEntity.ok(courierService.getCouriersByActive(isActive));
    }

    @PutMapping("/courier/{id}")
    public void updateCouriers(@PathVariable long id, @RequestBody CourierForUpdate courier) {
        courierService.updateCourier(id, courier);
    }

}
