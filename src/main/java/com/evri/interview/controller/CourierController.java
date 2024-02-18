package com.evri.interview.controller;

import com.evri.interview.model.Courier;
import com.evri.interview.service.CourierService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
@Validated
public class CourierController {

    private CourierService courierService;

    @GetMapping("/couriers")
    public ResponseEntity<List<Courier>> getAllCouriers(@RequestParam(required = false, defaultValue = "false") boolean isActive) {

        return ResponseEntity.ok(courierService.getAllCouriers(isActive));
    }

    @PutMapping("/couriers/{courierId}")
    public ResponseEntity<?> updateCourier(@PathVariable Long courierId, @RequestBody @Valid Courier courier) {

        courierService.updateCourierById(courierId, courier);

        return new ResponseEntity<>("Courier successfully updated.", HttpStatus.OK);
    }

}
