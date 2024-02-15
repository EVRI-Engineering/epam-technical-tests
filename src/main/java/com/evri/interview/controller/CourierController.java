package com.evri.interview.controller;

import com.evri.interview.model.Courier;
import com.evri.interview.service.CourierService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class CourierController {

    private CourierService courierService;

    /**
     *
     * @param isActive
     * @return all couriers depending on isActive param
     */
    @GetMapping("/couriers")
    public ResponseEntity<List<Courier>> getAllCouriers(
            @RequestParam(value = "isActive", required = false, defaultValue = "false") boolean isActive) {
        return ResponseEntity.ok(courierService.getCouriers(isActive));
    }

    /**
     *
     * @param courierId id of model for updating
     * @param updatedCourier updated courier model
     * @return updated model
     */
    @PutMapping("/couriers/{courierId}")
    public ResponseEntity<Courier> updateCourier(@PathVariable Long courierId, @RequestBody Courier updatedCourier) {
        if (courierId != updatedCourier.getId()) {
            throw new IllegalStateException("Invalid courier id");
        }
        return new ResponseEntity<>(courierService.updateCourier(courierId, updatedCourier), HttpStatus.OK);
    }
}
