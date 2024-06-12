package com.evri.interview.service;

import java.util.List;

import com.evri.interview.dto.CourierRequest;
import com.evri.interview.dto.CourierResponse;

public interface ICourierService {

    List<CourierResponse> getAllCouriers(boolean isActive);
    CourierResponse updateCourier(long courierId, CourierRequest courierRequest);
}
