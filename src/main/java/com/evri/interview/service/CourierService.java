package com.evri.interview.service;

import com.evri.interview.dto.CourierResponseDto;
import com.evri.interview.dto.CourierUpdateDto;

import java.util.List;

public interface CourierService {
    List<CourierResponseDto> getAllCouriers(boolean isActive);

    CourierResponseDto updateCourierById(long courierId, CourierUpdateDto courierUpdateDto);
}
