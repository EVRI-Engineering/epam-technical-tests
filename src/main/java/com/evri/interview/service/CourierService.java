package com.evri.interview.service;

import com.evri.interview.exception.EntityNotFoundException;
import com.evri.interview.model.Courier;

import java.util.List;

public interface CourierService {
    /**
     * Retrieves a list of all couriers from the database.
     *
     * @return A list of {@link Courier} entities.
     */
    List<Courier> getAllCouriers();

    /**
     * Retrieves a list of all active couriers from the database.
     * Active couriers are identified based on their 'active' status.
     *
     * @return A list of active {@link Courier} entities.
     */
    List<Courier> getActiveCouriers();

    /**
     * Updates an existing courier identified by the given ID with the new details provided in the {@link Courier} DTO.
     * If the courier with the specified ID does not exist, an {@link EntityNotFoundException} is thrown.
     *
     * @param courierDto The {@link Courier} DTO containing the updated fields for the courier.
     * @param id         The ID of the courier to be updated.
     * @return The updated {@link Courier} entity.
     * @throws EntityNotFoundException if the courier with the specified ID is not found.
     */
    Courier updateCourierById(Courier courierDto, Long id);

}
