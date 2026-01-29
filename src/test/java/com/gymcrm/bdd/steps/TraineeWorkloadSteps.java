package com.gymcrm.bdd.steps;

import com.gymcrm.domain.model.TrainingTypeEnum;
import com.gymcrm.presentation.dto.request.CreateTrainingDto;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import static org.awaitility.Awaitility.await;
import static java.time.Duration.ofSeconds;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TraineeWorkloadSteps {
    private final TestRestTemplate testRestTemplate;
    private final RestTemplate loadBalancedRestTemplate;

    @Value("${service-name}")
    private String SERVICE_NAME;

    @Autowired
    public TraineeWorkloadSteps(TestRestTemplate testRestTemplate, RestTemplate loadBalancedRestTemplate) {
        this.testRestTemplate = testRestTemplate;
        this.loadBalancedRestTemplate = loadBalancedRestTemplate;
    }

    @When("I add the following trainings:")
    public void i_add_the_following_trainings(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        for (Map<String, String> row : rows) {
            CreateTrainingDto dto = new CreateTrainingDto(
                    row.get("trainer"),
                    row.get("trainee"),
                    TrainingTypeEnum.valueOf(row.get("type")),
                    row.get("name"),
                    LocalDate.parse(row.get("date")),
                    Integer.parseInt(row.get("hours"))
            );

            testRestTemplate.postForEntity(getTrainingsUrl(), dto, Void.class);
        }
    }

    @When("I delete the trainee {string}")
    public void i_delete_the_trainee(String trainee) {
        testRestTemplate.exchange(getTraineeUrl(trainee), HttpMethod.DELETE, null, Void.class);
    }

    @Then("the total workload hours for trainer {string} for month {int} year {int} should be {int}")
    public void the_total_workload_hours_for_trainer_should_be(String trainer, int month, int year, int expectedHours) {
        await().atMost(ofSeconds(5)).untilAsserted(() -> {
            Map workload = loadBalancedRestTemplate.getForObject(getWorkloadUrl(trainer, month, year), Map.class);
            int totalHours = (int) workload.get("totalHours");
            assertEquals(expectedHours, totalHours);
        });
    }

    private String getTrainingsUrl() {
        return UriComponentsBuilder.fromUriString("/trainings")
                .toUriString();
    }

    private String getTraineeUrl(String trainee) {
        return UriComponentsBuilder.fromUriString("/trainees/{trainee}")
                .buildAndExpand(trainee)
                .toUriString();
    }

    private String getWorkloadUrl(String trainer, int month, int year) {
        return UriComponentsBuilder.fromUriString("http://" + SERVICE_NAME + "/workload/{trainer}")
                .queryParam("month", month)
                .queryParam("year", year)
                .buildAndExpand(trainer)
                .toUriString();
    }
}
