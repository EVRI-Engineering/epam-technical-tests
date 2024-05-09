package com.evri.interview.controller;

import com.evri.interview.exception.NotFoundException;
import com.evri.interview.model.Courier;
import com.evri.interview.model.SaveCourier;
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
    public ResponseEntity<List<Courier>> getAllCouriers(
            @RequestParam(defaultValue = "false") boolean isActive) {

        return ResponseEntity.ok(
                courierService.getAllCouriers(isActive));
    }

    @PutMapping("courier/{courierId}")
    public ResponseEntity<?> updateCourier(
            @PathVariable Long courierId, @RequestBody SaveCourier newCourier) {

        return courierService
                .updateCourier(courierId, newCourier)
                .map(ResponseEntity::ok)
                .orElseThrow(NotFoundException::new);


    }

}
