package com.gymcrm.component;

import com.gymcrm.infrastructure.security.service.JwtService;
import com.gymcrm.presentation.dto.response.TrainerWithTraineesDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TrainerSecurityComponentTest extends BaseComponentTest {
    private final JwtService jwtService;
    private static final String BASE_URL = "/trainers/";
    private static final String USER_ALEX = "Alex.Johnson";
    private static final String USER_JANE = "Jane.Smith";

    @Autowired
    public TrainerSecurityComponentTest(TestRestTemplate testRestTemplate, JwtService jwtService) {
        super(testRestTemplate);
        this.jwtService = jwtService;
    }

    @Test
    void getTrainerProfileWithoutTokenShouldReturn403() {
        ResponseEntity<Map> response = testRestTemplate.getForEntity(buildUrl(USER_ALEX), Map.class);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void getTrainerProfileWithInvalidTokenShouldReturn401() {
        ResponseEntity<Map> response = getWithToken(USER_ALEX, "invalid-token", Map.class);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void getTrainerProfileWithValidTokenForAnotherUserShouldReturn403() {
        String token = jwtService.generateTokenForUser(USER_ALEX);
        ResponseEntity<Map> response = getWithToken(USER_JANE, token, Map.class);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void getTrainerProfileWithValidTokenShouldReturn200() {
        String token = jwtService.generateTokenForUser(USER_ALEX);
        ResponseEntity<TrainerWithTraineesDto> response = getWithToken(USER_ALEX, token, TrainerWithTraineesDto.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        TrainerWithTraineesDto body = response.getBody();
        assertEquals("ZUMBA", body.getSpecialization());
        assertEquals(USER_ALEX, body.getFirstName() + "." + body.getLastName());
        assertTrue(body.isActive());
    }

    private String buildUrl(String username) {
        return BASE_URL + username;
    }

    private <T> ResponseEntity<T> getWithToken(String username, String token, Class<T> responseType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        return testRestTemplate.exchange(buildUrl(username), HttpMethod.GET, entity, responseType);
    }
}
