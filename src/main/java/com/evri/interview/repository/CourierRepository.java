package com.evri.interview.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourierRepository extends JpaRepository<CourierEntity, Long> {

    @Query("SELECT c FROM CourierEntity c WHERE c.active = :isActive")
    List<CourierEntity> findAllByActivity(@Param("isActive") boolean isActive);
}
