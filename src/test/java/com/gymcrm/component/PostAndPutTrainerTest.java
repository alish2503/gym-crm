package com.gymcrm.component;

import com.gymcrm.domain.model.TrainingTypeEnum;
import com.gymcrm.presentation.dto.request.CreateTrainerDto;
import com.gymcrm.presentation.dto.request.UpdateTrainerDto;
import com.gymcrm.presentation.dto.response.TrainerWithTraineesAfterUpdateDto;
import com.gymcrm.presentation.dto.response.UserCredentialsDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PostAndPutTrainerTest extends BaseComponentTest {
    private static final String BASE_URL = "/trainers";
    private static final String TRAINER_JOHN_DOE = "John.Doe";
    private static final String FIRST_NAME = "John";
    private static final String LAST_NAME = "Doe";
    private static final String UPDATED_LAST_NAME = "Updated";

    @Autowired
    public PostAndPutTrainerTest(TestRestTemplate testRestTemplate) {
        super(testRestTemplate);
    }

    @Test
    void shouldRegisterTrainerSuccessfully() {
        CreateTrainerDto request = new CreateTrainerDto(FIRST_NAME, LAST_NAME, TrainingTypeEnum.FITNESS);
        ResponseEntity<UserCredentialsDto> response = testRestTemplate.postForEntity(
                buildRegisterUrl(), request, UserCredentialsDto.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void shouldReturn400WhenTrainingTypeIsInvalid() {
        String json = """
        {
          "firstName": "%s",
          "lastName": "%s",
          "specialization": "WRONG_TYPE"
        }
        """.formatted(FIRST_NAME, LAST_NAME);

        ResponseEntity<Map<String, String>> response = postJson(buildRegisterUrl(), json);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void shouldUpdateTrainerSuccessfully() {
        UpdateTrainerDto request = new UpdateTrainerDto(FIRST_NAME, UPDATED_LAST_NAME, false, TrainingTypeEnum.YOGA);
        ResponseEntity<TrainerWithTraineesAfterUpdateDto> response = put(buildTrainerUrl(), request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        TrainerWithTraineesAfterUpdateDto body = response.getBody();
        assertNotNull(body);
        assertEquals(TRAINER_JOHN_DOE, body.getUsername());
        assertEquals(FIRST_NAME, body.getFirstName());
        assertEquals(UPDATED_LAST_NAME, body.getLastName());
        assertEquals("YOGA", body.getSpecialization());
        assertFalse(body.isActive());
    }

    private String buildRegisterUrl() {
        return UriComponentsBuilder.fromUriString(BASE_URL + "/register").toUriString();
    }

    private String buildTrainerUrl() {
        return UriComponentsBuilder.fromUriString(BASE_URL + "/" + PostAndPutTrainerTest.TRAINER_JOHN_DOE).toUriString();
    }

    private <T> ResponseEntity<T> postJson(String url, String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(json, headers);
        return testRestTemplate.postForEntity(url, entity, (Class<T>) Map.class);
    }

    private <T> ResponseEntity<T> put(String url, Object request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> entity = new HttpEntity<>(request, headers);
        return testRestTemplate.exchange(url, HttpMethod.PUT, entity, (Class<T>) TrainerWithTraineesAfterUpdateDto.class);
    }
}
