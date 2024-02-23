package com.evri.interview.repository;

import com.evri.interview.repository.entity.CourierEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourierRepository extends JpaRepository<CourierEntity, Long> {

    List<CourierEntity> findByActiveTrue();

}
