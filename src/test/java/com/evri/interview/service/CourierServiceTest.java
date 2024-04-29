package com.evri.interview.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.evri.interview.exception.CourierNotFound;
import com.evri.interview.model.FullCourierDto;
import com.evri.interview.model.ShortCourierDto;
import com.evri.interview.repository.CourierEntity;
import com.evri.interview.repository.CourierRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(CourierService.class)
public class CourierServiceTest {

  @MockBean
  private CourierTransformer courierTransformer;

  @Autowired
  private CourierRepository courierRepository;

  @Autowired
  private CourierService courierService;

  @Test
  public void whenGetAllActiveCouriersIsCalled_thenReturnAllActiveCouriers() {
    List<ShortCourierDto> result = courierService.getAllCouriers(true);
    assertEquals(3, result.size());
  }

  @Test
  public void whenFullCourierDtoIsPassed_thenUpdateCourierSuccessfully() {
    CourierEntity courierEntity = new CourierEntity(1L, "firstname", "lastname", true);
    CourierEntity updatedEntity = courierRepository.save(courierEntity);

    FullCourierDto courierDto = new FullCourierDto("updated firstname", "updated lastname", false);
    ShortCourierDto updatedCourierDto = new ShortCourierDto(1L,
        "updated firstname updated lastname", false);

    when(courierTransformer.toCourier(updatedEntity)).thenReturn(updatedCourierDto);

    ShortCourierDto result = courierService.updateCourier(1L, courierDto);
    assertEquals("updated firstname updated lastname", result.getName());
    assertFalse(result.isActive());
  }

  @Test
  public void whenCourierIsNotFound_thenUpdateHasToFailWith404() {
    FullCourierDto courierDto = new FullCourierDto("updated firstname", "updated lastname", false);

    Exception exception = assertThrows(CourierNotFound.class, () ->
        courierService.updateCourier(20L, courierDto));
    assertEquals("Courier with id: 20 has not been found", exception.getMessage());
  }
}