package com.evri.interview.controller;

import com.evri.interview.model.Courier;
import com.evri.interview.model.UpdateCourier;
import com.evri.interview.service.CourierService;

import javax.validation.Valid;
import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
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

  @GetMapping("/couriers")
  @ResponseStatus(HttpStatus.OK)
  public List<Courier> getAllCouriers(
      @RequestParam(value = "isActive", required = false) boolean isActive) {
    return courierService.getAllCouriers(isActive);
  }

  @PutMapping("/couriers/{courierId}")
  @ResponseStatus(HttpStatus.OK)
  public Courier updateCourier(
      @PathVariable("courierId") long courierId, @RequestBody @Valid UpdateCourier courier) {
    return courierService.updateCourier(courierId, courier);
  }
}
