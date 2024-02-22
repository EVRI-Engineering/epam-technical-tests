package com.evri.interview.service;

import com.evri.interview.model.Courier;
import com.evri.interview.model.CourierRequestDto;

import javax.transaction.Transactional;
import java.util.List;

public interface CourierService {
    List<Courier> getAllCouriers(boolean isActive);

    @Transactional
    Courier updateCourierById(long courierId, CourierRequestDto courierRequestDto);
}
