package com.evri.interview.controller;

import java.util.List;

import javax.validation.Valid;

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
import org.springframework.web.server.ResponseStatusException;

import com.evri.interview.dto.CourierDto;
import com.evri.interview.model.Courier;
import com.evri.interview.service.CourierService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
@Validated
public class CourierController {

    private CourierService courierService;

    @GetMapping("/couriers")
    public ResponseEntity<List<Courier>> getAllCouriers(
            @RequestParam(name = "onlyActive", required = false, defaultValue = "false") boolean onlyActive) {
        return ResponseEntity.ok(courierService.getCouriers(onlyActive));
    }

    @PutMapping("/couriers/{courierId}")
    public ResponseEntity<Courier> updateCourier(@PathVariable("courierId") Long courierId,
            @RequestBody @Valid CourierDto courierDto) {
        return ResponseEntity.ok(courierService
                .updateCourier(courierId, courierDto.getFirstName(), courierDto.getLastName(), courierDto.getActive())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }
}
