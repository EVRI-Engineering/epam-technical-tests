package com.evri.interview.controller;

import javax.validation.Valid;

import com.evri.interview.dto.CourierRequest;
import com.evri.interview.dto.ErrorResponse;
import com.evri.interview.dto.HttpResponse;
import com.evri.interview.dto.CourierResponse;
import com.evri.interview.service.ICourierService;
import com.evri.interview.utils.RestUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/couriers")
@Slf4j
public class CourierController {

    private final ICourierService courierService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HttpResponse<List<CourierResponse>, Map<String, String>>> getAllCouriers(
            @RequestParam(required = false, defaultValue = "false") boolean isActive) {
        return HttpResponse.ok(courierService.getAllCouriers(isActive));
    }

    @PutMapping(value = "/{courierId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HttpResponse<CourierResponse, Map<String, String>>> updateCourierById(
            @PathVariable long courierId, @RequestBody @Valid CourierRequest courierRequest,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.info("Validation was failed for courier with id={}", courierId);
            final ErrorResponse<Map<String, String>> errorResponse = RestUtils.mapErrorResponse(bindingResult);
            return HttpResponse.error(errorResponse);
        }
        return HttpResponse.ok(courierService.updateCourier(courierId, courierRequest));
    }

}
