package com.evri.interview.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class CourierRepositoryTest {

  @Autowired
  CourierRepository tested;


  void shouldFindById() {
    CourierEntity entity1 = CourierEntity.builder().firstName("fn1").build();
    CourierEntity entity2 = CourierEntity.builder().firstName("fn2").build();
    CourierEntity entity3 = CourierEntity.builder().firstName("fn3").build();

    tested.save(entity2);

    Optional<CourierEntity> actual = tested.findById(444l);
    assertThat(actual.get()).isEqualTo(CourierEntity.builder().id(444l).build());
  }
}