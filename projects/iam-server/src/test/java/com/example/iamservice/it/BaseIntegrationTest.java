package com.example.iamservice.it;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public abstract class BaseIntegrationTest {

    private static PostgreSQLContainer<?> postgres;

    public static PostgreSQLContainer<?> getPostgres() {
        if (postgres == null) {
            synchronized (BaseIntegrationTest.class) {
                if (postgres == null) {
                    postgres = new PostgreSQLContainer<>("postgres:16")
                            .withDatabaseName("iam_test")
                            .withUsername("test")
                            .withPassword("test");
                }
            }
        }
        return postgres;
    }

    @BeforeAll
    static void startPostgres() {
        getPostgres().start();
    }

    @AfterAll
    static void stopPostgres() {
        // Don't stop here — shared across all test classes in this build
        // Container will be stopped by Testcontainers' Ryuk on JVM exit
    }

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", () -> getPostgres().getJdbcUrl());
        registry.add("spring.datasource.username", () -> getPostgres().getUsername());
        registry.add("spring.datasource.password", () -> getPostgres().getPassword());
    }
}
