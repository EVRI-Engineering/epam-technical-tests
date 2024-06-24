package com.evri.interview;

import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql(value = "/sql/clean.sql", executionPhase = AFTER_TEST_METHOD)
class ApplicationTest {

    @Test
    void contextLoads() {
    }

}
