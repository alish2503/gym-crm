package com.gymcrm.integration;

import com.gymcrm.BaseTest;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.RabbitMQContainer;

public abstract class BaseIntegrationTest extends BaseTest {

    @ServiceConnection
    private final static RabbitMQContainer rabbitMQContainer = new RabbitMQContainer("rabbitmq:3-management");

    protected BaseIntegrationTest(TestRestTemplate testRestTemplate) {
        super(testRestTemplate);
    }
}
