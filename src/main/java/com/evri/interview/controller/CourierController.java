package com.evri.interview.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.evri.interview.exception.CourierNotFoundException;
import com.evri.interview.model.Courier;
import com.evri.interview.model.CourierPutDataModel;
import com.evri.interview.service.CourierService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class CourierController {

    private CourierService courierService;

    @GetMapping("/couriers")
    public ResponseEntity<List<Courier>> getAllCouriers(
        @RequestParam(name = "isActive", required = false, defaultValue = "false") final boolean isActive) {
        return ResponseEntity.ok(courierService.getAllCouriers(isActive));
    }

    @PutMapping("/couriers/{id}")
    public ResponseEntity<Courier> updateCourier(
        @PathVariable("id") final Long id,
        @RequestBody CourierPutDataModel courierPutDataModel) {

        try {
            courierService.updateCourierData(id, courierPutDataModel);
        } catch (final CourierNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().build();
    }
}
