package com.evri.interview.service;

import com.evri.interview.model.Courier;

import java.util.List;

public interface CourierService {
    List<Courier> getAllCouriers();

    List<Courier> getActiveCouriers();

    Courier updateCourierById(Courier courierDto, Long id);

}
