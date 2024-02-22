package com.evri.interview.controller;

import com.evri.interview.exception.ApiCustomException;
import com.evri.interview.model.Courier;
import com.evri.interview.model.CourierRequestDto;
import com.evri.interview.service.CourierService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/couriers")
public class CourierController {

    private CourierService courierService;

    @GetMapping()
    public ResponseEntity<List<Courier>> getAllCouriers(@RequestParam(name = "isActive", required = false) boolean isActive) {
        return ResponseEntity.ok(courierService.getAllCouriers(isActive));
    }

    @PutMapping("/{courierId}")
    public ResponseEntity<Courier> updateCourierById(@PathVariable long courierId, @RequestBody @Valid CourierRequestDto courierRequestDto) throws ApiCustomException {
        Courier courier = courierService.updateCourierById(courierId, courierRequestDto);
        return ResponseEntity.ok().body(courier);
    }

}
