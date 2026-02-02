package com.gymcrm;

import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
@ActiveProfiles("test")
public abstract class BaseTest {

    @ServiceConnection
    private final static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:16");
    protected final TestRestTemplate testRestTemplate;

    public BaseTest(TestRestTemplate testRestTemplate) {
        this.testRestTemplate = testRestTemplate;
    }
}
