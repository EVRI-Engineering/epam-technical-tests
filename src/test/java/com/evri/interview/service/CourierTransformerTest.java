package com.evri.interview.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.evri.interview.model.Courier;
import com.evri.interview.repository.CourierEntity;

@ExtendWith(MockitoExtension.class)
class CourierTransformerTest {

  CourierTransformer tested = new CourierTransformer();

  @Test
  void shouldTransformToEntity() {
    CourierEntity result = tested.toCourierEntity(Courier.builder().id(1).name("Ihor Danchuk").build());
    assertThat(result.getFirstName()).isEqualTo("Ihor");
    assertThat(result.getLastName()).isEqualTo("Danchuk");
  }
}