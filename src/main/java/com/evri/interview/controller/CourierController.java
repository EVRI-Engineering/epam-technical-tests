package com.evri.interview.controller;

import com.evri.interview.model.FullCourierDto;
import com.evri.interview.model.ShortCourierDto;
import com.evri.interview.service.CourierService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class CourierController {

  private CourierService courierService;

  @GetMapping("/couriers")
  public ResponseEntity<List<ShortCourierDto>> getAllCouriers(
      @RequestParam(required = false) boolean isActive) {
    return ResponseEntity.ok(courierService.getAllCouriers(isActive));
  }

  @PutMapping("/couriers/{courierId}")
  public ResponseEntity<ShortCourierDto> updateCourier(@PathVariable long courierId,
      @RequestBody FullCourierDto courierDto) {
    return ResponseEntity.ok(courierService.updateCourier(courierId, courierDto));
  }

}
