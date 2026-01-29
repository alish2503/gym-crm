package com.gymcrm.bdd.config;

import com.gymcrm.testconfig.DisabledAuthorizationTestConfig;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
@ActiveProfiles({"local", "no-security"})
@Import(DisabledAuthorizationTestConfig.class)
public class CucumberSpringConfig {}
