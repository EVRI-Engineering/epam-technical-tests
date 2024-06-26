package com.evri.interview.controller;

import com.evri.interview.model.Courier;
import com.evri.interview.service.CourierService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;


import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/couriers")
public class CourierController {

    private static final String UPDATE_MESSAGE = "Courier with id %s is updated";

    private CourierService courierService;

    @GetMapping
    public ResponseEntity<List<Courier>> getAllCouriers(
            @RequestParam(required = false, defaultValue = "false") boolean isOnlyActive) {

        return ResponseEntity.ok(courierService.getAllCouriers(isOnlyActive));
    }

    @PutMapping("{id}")
    public ResponseEntity<String> updateCouriers(@PathVariable Long id, @RequestBody Courier data) {
        courierService.updateCourier(id, data);
        return ResponseEntity.ok()
                .body(String.format(UPDATE_MESSAGE, id));
    }

}
