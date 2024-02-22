package com.evri.interview.controller;

import com.evri.interview.dto.CourierDto;
import com.evri.interview.model.Courier;
import com.evri.interview.service.CourierService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import javax.validation.constraints.NotNull;
import java.util.List;

/*
try it out on http://localhost:8080/swagger-ui/index.html
 */
@RestController
@AllArgsConstructor
@Validated
@RequestMapping("/api")
@Slf4j
public class CourierController {

    private CourierService courierService;

    @GetMapping("/couriers")
    public ResponseEntity<List<Courier>> getAllActiveCouriers(@RequestParam(value = "isActive", required = false, defaultValue = "false") boolean isActive) {
        return ResponseEntity.ok(courierService.getAllActiveCouriers(isActive));
    }

    @PutMapping("/couriers/{id}")
    public ResponseEntity<String> updateCourier(@NotNull @PathVariable Long id, @RequestBody @Valid CourierDto dto) {
        log.info("Request on update courier with id {} was received", id);
        courierService.updateCourier(id, dto);
        return ResponseEntity.ok().body(String.format("Courier with id %s is updated", id));
    }
}
