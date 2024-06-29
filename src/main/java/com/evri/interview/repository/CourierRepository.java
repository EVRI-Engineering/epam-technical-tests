package com.evri.interview.repository;

import java.util.List;
import java.util.Optional;
import javax.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

@Repository
public interface CourierRepository extends JpaRepository<CourierEntity, Long> {

	List<CourierEntity> getAllByActiveIsTrue();

	@Lock(LockModeType.PESSIMISTIC_READ)
	Optional<CourierEntity> findById(long id);
}
