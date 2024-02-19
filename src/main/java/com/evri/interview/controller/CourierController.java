package com.evri.interview.controller;

import com.evri.interview.dto.CourierResponseDto;
import com.evri.interview.dto.CourierUpdateDto;
import com.evri.interview.service.CourierService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/couriers")
public class CourierController {

    private CourierService courierService;

    @GetMapping
    public List<CourierResponseDto> getAllCouriers(@RequestParam(required = false) boolean isActive) {
        return courierService.getAllCouriers(isActive);
    }

    @PutMapping("/{courierId}")
    public CourierResponseDto updateCourierById(@PathVariable long courierId,
                                                @RequestBody @Valid CourierUpdateDto courierUpdateDto) {
        return courierService.updateCourierById(courierId, courierUpdateDto);
    }
}
