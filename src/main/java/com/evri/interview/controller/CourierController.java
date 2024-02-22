package com.evri.interview.controller;

import com.evri.interview.model.Courier;
import com.evri.interview.service.CourierService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Controller for managing {@link Courier} entities. This class handles API requests for retrieving
 * and updating couriers within the application.
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class CourierController {

    private CourierService courierService;

    @Operation(summary = "Get a list of couriers", description = "Returns all couriers or only active couriers based on the 'isActive' query parameter.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of couriers"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/couriers")
    public ResponseEntity<List<Courier>> getCouriers(@RequestParam(name = "isActive", required = false) Boolean isActive) {
        List<Courier> couriers;
        if (Boolean.TRUE.equals(isActive)) {
            couriers = courierService.getActiveCouriers();
        } else {
            couriers = courierService.getAllCouriers();
        }
        return ResponseEntity.ok(couriers);
    }

    @Operation(summary = "Update a courier", description = "Updates a courier with a given ID. If the courier does not exist, an 404 status is returned.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated the courier"),
            @ApiResponse(responseCode = "404", description = "The courier with the specified ID was not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/courier/{id}")
    public ResponseEntity<Courier> updateCourier(@RequestBody @Valid Courier courier, @PathVariable Long id) {
        Courier updatedCourier = courierService.updateCourierById(courier, id);
        return ResponseEntity.ok(updatedCourier);
    }
}
