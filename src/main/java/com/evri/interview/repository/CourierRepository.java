package com.evri.interview.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for {@link CourierEntity} instances.
 * Provides automatic CRUD functionality and custom queries for courier entities.
 */
@Repository
public interface CourierRepository extends JpaRepository<CourierEntity, Long> {

    /**
     * Retrieves a list of {@link CourierEntity} instances based on their active status.
     *
     * @param active A boolean value indicating the active status of couriers to be retrieved.
     *               If {@code true}, only active couriers are returned.
     *               If {@code false}, only inactive couriers are returned.
     * @return A list of {@link CourierEntity} instances that match the specified active status.
     */
    List<CourierEntity> findAllByActive(boolean active);
}
