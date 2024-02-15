package com.evri.interview.controller;

import com.evri.interview.dto.CourierRequestDto;
import com.evri.interview.dto.CourierResponseDto;
import com.evri.interview.service.CourierService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class CourierController {

    private CourierService courierService;

    @GetMapping("/couriers")
    public ResponseEntity<List<CourierResponseDto>> getAllCouriers(@RequestParam(required = false) boolean isActive) {

        return ResponseEntity.ok(courierService.getAllCouriers(isActive));
    }

    @PutMapping("/couriers/{id}")
    public ResponseEntity<CourierResponseDto> updateCourierById(@PathVariable("id") long id, @RequestBody @Valid CourierRequestDto courierRequestDto) {
        return ResponseEntity.ok(courierService.updateCourierById(id, courierRequestDto));
    }

}
