package com.evri.interview.repository;

import com.evri.interview.repository.entity.CourierEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class CourierRepositoryTest {

    @Autowired
    private CourierRepository courierRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    void test_shouldSave_CourierEntity() {
        CourierEntity courierEntity = CourierEntity.builder()
                .id(1)
                .firstName("FirstName")
                .lastName("LastName")
                .active(false)
                .build();
        CourierEntity actualEntity = courierRepository.save(courierEntity);
        CourierEntity expected = testEntityManager.find(CourierEntity.class, actualEntity.getId());
        assertEquals(expected, actualEntity);
    }

    @Test
    void test_shouldReturn_listOfCourierEntities_withActiveIsTrue() {
        CourierEntity courierEntity = CourierEntity.builder()
                .id(2)
                .firstName("FirstName")
                .lastName("LastName")
                .active(true)
                .build();
        testEntityManager.merge(courierEntity);

        CriteriaBuilder cb = testEntityManager.getEntityManager()
                .getCriteriaBuilder();
        CriteriaQuery<CourierEntity> query = cb.createQuery(CourierEntity.class);
        Root<CourierEntity> from = query.from(CourierEntity.class);
        query.where(cb.isTrue(from.get("active")));

        List<CourierEntity> expectedCourierList = testEntityManager.getEntityManager().createQuery(query).getResultList();
        List<CourierEntity> actualCuirestList = courierRepository.findByActiveTrue();

        assertEquals(expectedCourierList, actualCuirestList);
    }

    @Test
    void test_shouldReturn_ListOfAllCourierEntities() {
        CriteriaQuery<CourierEntity> query = testEntityManager.getEntityManager().getCriteriaBuilder().createQuery(CourierEntity.class);
        query.from(CourierEntity.class);

        List<CourierEntity> expectedCourierList = testEntityManager.getEntityManager().createQuery(query).getResultList();
        List<CourierEntity> actualCuirestList = courierRepository.findAll();

        assertEquals(expectedCourierList, actualCuirestList);
    }
}