package com.eventos.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class DatabaseTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void testDatabaseConnection() {
        String version = jdbcTemplate.queryForObject("SELECT version()", String.class);
        System.out.println("Conexión exitosa a PostgreSQL");
        System.out.println("Versión: " + version);
        assertNotNull(version);
    }
}