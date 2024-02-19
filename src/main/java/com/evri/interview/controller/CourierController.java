package com.evri.interview.controller;

import com.evri.interview.model.Courier;
import com.evri.interview.request.CourierRequestBody;
import com.evri.interview.service.CourierService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class CourierController {

    private CourierService courierService;

    @GetMapping("/couriers")
    public ResponseEntity<List<Courier>> getAllCouriers() {

        return ResponseEntity.ok(courierService.getAllCouriers());
    }

    /**
     * Updates a courier's information by their ID.
     * 
     * This endpoint allows clients to update the information of a courier identified
     * by the specified courier ID.
     * 
     * @param courierId The unique identifier of the courier.
     * @param courierRequestBody contains the updated information for the courier.
     * @return A ResponseEntity with the updated Courier object and an HTTP status code.
     *          Returns 200 (OK) if the update is successful.
     *          Returns 404 (Not Found) if the courier with the specified ID is not found.
     */
    @PutMapping("/couriers/{courierId}")
    public ResponseEntity<Courier> updateCourierById(
        @PathVariable long courierId,
        @RequestBody @Valid CourierRequestBody courierRequestBody
    ) {
        return ResponseEntity.ok(courierService.updateCourierById(courierId, courierRequestBody));
    }

}
