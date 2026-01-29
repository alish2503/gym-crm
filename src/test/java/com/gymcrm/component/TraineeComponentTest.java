package com.gymcrm.component;

import com.gymcrm.presentation.dto.request.UpdateTrainersDto;
import com.gymcrm.presentation.dto.response.TrainerDto;
import com.gymcrm.testconfig.NoSecurityTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@NoSecurityTest
class TraineeComponentTest extends BaseComponentTest {
    private static final String BASE_URL = "/trainees/";
    private static final String TRAINEE_EMMA = "Emma.Brown";
    private static final String TRAINER_JOHN = "John.Doe";
    private static final String TRAINER_JANE = "Jane.Smith";
    private static final String TRAINER_NON_EXISTENT = "Non.Existent";

    @Autowired
    public TraineeComponentTest(TestRestTemplate testRestTemplate) {
        super(testRestTemplate);
    }

    @Test
    void shouldUpdateTraineeTrainersSuccessfully() {
        UpdateTrainersDto request = new UpdateTrainersDto();
        request.setTrainerUsernames(List.of(TRAINER_JOHN, TRAINER_JANE));
        ResponseEntity<List<TrainerDto>> response = putForTrainers(request, new ParameterizedTypeReference<>() {});
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<TrainerDto> body = response.getBody();
        assertNotNull(body);
        assertEquals(2, body.size());
        List<String> returnedUsernames = body.stream().map(TrainerDto::getUsername).toList();
        assertTrue(returnedUsernames.contains(TRAINER_JOHN));
        assertTrue(returnedUsernames.contains(TRAINER_JANE));
    }

    @Test
    void shouldReturn400WhenTrainerListIsEmpty() {
        UpdateTrainersDto request = new UpdateTrainersDto();
        request.setTrainerUsernames(List.of());
        ResponseEntity<Map<String, String>> response = putForTrainers(request);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void shouldReturn404WhenTrainerListIsIncorrect() {
        UpdateTrainersDto request = new UpdateTrainersDto();
        request.setTrainerUsernames(List.of(TRAINER_NON_EXISTENT));
        ResponseEntity<Map<String, String>> response = putForTrainers(request);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    private <T> ResponseEntity<T> putForTrainers(UpdateTrainersDto request) {
        return testRestTemplate.exchange(buildUrl(), HttpMethod.PUT, buildEntity(request), (Class<T>) Map.class);
    }

    private <T> ResponseEntity<T> putForTrainers(UpdateTrainersDto request, ParameterizedTypeReference<T> responseType) {
        return testRestTemplate.exchange(buildUrl(), HttpMethod.PUT, buildEntity(request), responseType);
    }

    private HttpEntity<UpdateTrainersDto> buildEntity(UpdateTrainersDto request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(request, headers);
    }

    private String buildUrl() {
        return UriComponentsBuilder.fromUriString(BASE_URL + TRAINEE_EMMA + "/trainers")
                .toUriString();
    }
}
