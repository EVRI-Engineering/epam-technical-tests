package com.evri.interview.controller;

import com.evri.interview.model.Courier;
import com.evri.interview.repository.CourierEntity;
import com.evri.interview.service.CourierService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class CourierController {

    private CourierService courierService;

    @GetMapping("/couriers")
    public ResponseEntity<List<Courier>> getAllCouriers(
            @RequestParam(required = false, defaultValue = "false") Boolean isActive) {
        return ResponseEntity.ok(courierService.getAllCouriers(isActive));
    }

    @PutMapping("/couriers/{id}")
    public ResponseEntity<Courier> updateCourier(@NonNull @PathVariable Long id, @NonNull @Valid @RequestBody Courier courier) {

        final Optional<CourierEntity> courierEntity = courierService.findById(id);

        if (courierEntity.isPresent()) {
            courier.setId(id);
            Courier result = courierService.update(courier);
            return ResponseEntity.ok(result);
        }

        return ResponseEntity.notFound().build();
    }

}
