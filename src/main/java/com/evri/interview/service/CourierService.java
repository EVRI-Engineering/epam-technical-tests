package com.evri.interview.service;

import com.evri.interview.model.Courier;
import com.evri.interview.model.UpdateCourierData;

import java.util.List;

public interface CourierService {
    List<Courier> getAllCouriers();

    List<Courier> getAllCouriersByActivity(boolean isActive);

    Courier updateById(long id, UpdateCourierData data);
}
