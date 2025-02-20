package com.evri.interview.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.evri.interview.model.Courier;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class CourierServiceIntegrationTest {

  @Autowired
  private CourierService courierService;

  @Test
  public void shouldReturnAllCouriers_WhenIsActiveIsFalse() {
    List<Courier> couriers = courierService.getAllCouriers(false);
    assertEquals(3, couriers.size(),
        "Should return all couriers since isActive is false");
    assertEquals("Ben", couriers.get(0).getFirstName());
    assertEquals("Askew", couriers.get(0).getLastName());
    assertEquals("John", couriers.get(1).getFirstName());
    assertEquals("Doe", couriers.get(1).getLastName());
  }

  @Test
  public void shouldReturnOnlyActiveCouriers_WhenIsActiveIsTrue() {
    List<Courier> couriers = courierService.getAllCouriers(true);
    assertEquals(2, couriers.size(),
        "Should return only active couriers since isActive is true");
    assertEquals("Ben", couriers.get(0).getFirstName());
    assertEquals("Askew", couriers.get(0).getLastName());
    assertEquals("Bill", couriers.get(1).getFirstName());
    assertEquals("Smith", couriers.get(1).getLastName());
  }

  @Test
  public void shouldUpdateCourierDetails_WhenCourierExists() {
    Courier updateCourierDetails = Courier.builder()
        .id(1L)
        .firstName("Andrew")
        .lastName("Jackson")
        .active(true)
        .build();

    Optional<Courier> updatedCourier =
        courierService.updateCourier(1L, updateCourierDetails);

    assertTrue(updatedCourier.isPresent());
    assertEquals("Andrew", updatedCourier.get().getFirstName());
    assertEquals("Jackson", updatedCourier.get().getLastName());
    assertTrue(updatedCourier.get().isActive());
  }

  @Test
  public void shouldReturnEmpty_WhenCourierDoesNotExist() {
    Courier updateCourierDetails = Courier.builder()
        .id(99L)
        .firstName("Invalid")
        .lastName("User")
        .active(true)
        .build();

    Optional<Courier> updatedCourier =
        courierService.updateCourier(99L, updateCourierDetails);

    assertFalse(updatedCourier.isPresent());
  }
}
