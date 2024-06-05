package com.evri.interview.service;

import com.evri.interview.exception.CourierException;
import com.evri.interview.exception.ExceptionReason;
import com.evri.interview.exception.ExceptionTitle;
import com.evri.interview.model.Courier;
import com.evri.interview.repository.CourierEntity;
import com.evri.interview.repository.CourierRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class CourierService {

  private CourierRepository repository;
  private CourierTransformer courierTransformer;

  public List<Courier> getAllCouriers(Boolean isActive) {
    List<CourierEntity> couriers;

    try {
      if (Boolean.TRUE.equals(isActive)) {
        couriers = repository.findByActiveTrue();
      } else {
        couriers = repository.findAll();
      }
    } catch (Exception ex) {
      log.error("Unable to get couriers from database.", ex);

      throw new CourierException(
          ExceptionReason.INTERNAL_SERVICE_ERROR,
          HttpStatus.INTERNAL_SERVER_ERROR,
          ExceptionTitle.DATABASE_ERROR.getTitle());
    }

    if (couriers.isEmpty()) {
      String msg =
          Boolean.TRUE.equals(isActive) ? "No active couriers found." : "No couriers found.";
      log.info(msg);
    }

    return couriers
        .stream()
        .map(courierTransformer::toCourier)
        .collect(Collectors.toList());
  }

  public Courier updateCourier(Long id, Courier courier) {
    Optional<CourierEntity> sourceCourier = getCourierById(id);

    if (!sourceCourier.isPresent()) {
      log.error(String.format("Courier with id '%s' does not exists.", id));

      throw new CourierException(
          ExceptionReason.COURIER_KEY_NOT_FOUND,
          HttpStatus.NOT_FOUND,
          ExceptionTitle.NOT_FOUND.getTitle());
    }

    Courier currentCourier = courierTransformer.toCourier(sourceCourier.get());
    if (currentCourier.compareTo(courier) == 0) {
      return currentCourier;
    }

    CourierEntity updatedCourier = updateAndSave(sourceCourier.get(), courier);

    return courierTransformer.toCourier(updatedCourier);
  }

  private Optional<CourierEntity> getCourierById(Long id) {
    Optional<CourierEntity> sourceCourier;

    try {
      sourceCourier = repository.findById(id);
    } catch (Exception ex) {
      log.error("Unable to get courier from database.", ex);

      throw new CourierException(
          ExceptionReason.INTERNAL_SERVICE_ERROR,
          HttpStatus.INTERNAL_SERVER_ERROR,
          ExceptionTitle.DATABASE_ERROR.getTitle());
    }

    return sourceCourier;
  }

  private CourierEntity updateAndSave(CourierEntity entity, Courier courier) {
    courierTransformer.update(entity, courier);

    try {
      return repository.save(entity);
    } catch (Exception ex) {
      log.error("Unable to save courier into database.", ex);

      throw new CourierException(
          ExceptionReason.INTERNAL_SERVICE_ERROR,
          HttpStatus.INTERNAL_SERVER_ERROR,
          ExceptionTitle.DATABASE_ERROR.getTitle());
    }
  }
}
