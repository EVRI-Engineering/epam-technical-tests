package com.evri.interview.controller;

import com.evri.interview.model.Courier;
import com.evri.interview.model.UpdateCourierData;
import com.evri.interview.service.CourierService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
@Slf4j
public class CourierController {

    private CourierService courierService;

    @GetMapping("/couriers")
    public ResponseEntity<List<Courier>> getAllCouriers(@RequestParam(required = false) Boolean isActive) {
        log.info("Get getAllCouriers request. isActive flag = '{}'", isActive);

        List<Courier> couriers = isActive != null ?
                courierService.getAllCouriersByActivity(isActive) :
                courierService.getAllCouriers();

            return ResponseEntity.ok(couriers);
    }

    @PutMapping("/couriers/{id}")
    public ResponseEntity<Courier> updateCourierById(@PathVariable long id, @Valid @RequestBody UpdateCourierData data) {
        log.info("Get updateCourierById request. CourierId = '{}'", id);

        return ResponseEntity.ok(courierService.updateById(id, data));
    }

}
