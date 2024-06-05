package com.evri.interview.controller;

import com.evri.interview.model.Courier;
import com.evri.interview.model.constraints.UpdateRequest;
import com.evri.interview.service.CourierService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@AllArgsConstructor
@RequestMapping("/api/couriers")
public class CourierController {

  private CourierService courierService;

  @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
  public ResponseEntity<List<Courier>> getAllCouriers(
      @RequestParam(required = false) Boolean isActive) {
    return ResponseEntity.ok(courierService.getAllCouriers(isActive));
  }

  @PutMapping(
      value = "/{id}",
      consumes = {MediaType.APPLICATION_JSON_VALUE},
      produces = {MediaType.APPLICATION_JSON_VALUE})
  public ResponseEntity<Courier> updateCourier(@PathVariable("id") Long id,
      @RequestBody @Validated(UpdateRequest.class) Courier courier) {
    return ResponseEntity.ok(courierService.updateCourier(id, courier));
  }
}
