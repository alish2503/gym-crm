package com.gymcrm.infrastructure.adapter;

import com.gymcrm.infrastructure.port.TrainerWorkloadClient;
import com.gymcrm.infrastructure.security.service.port.JwtService;
import com.gymcrm.presentation.dto.request.TrainerWorkloadEventDto;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * @author Alish
 */

@Slf4j
@Component
public class TrainerWorkloadClientImpl implements TrainerWorkloadClient {

    private final JwtService jwtService;
    private final RestTemplate restTemplate;
    private final String workloadServiceUrl;

    @Autowired
    public TrainerWorkloadClientImpl(JwtService jwtService, RestTemplate restTemplate,
                                     @Value("${workload.service.url}") String workloadServiceUrl) {
        this.jwtService = jwtService;
        this.restTemplate = restTemplate;
        this.workloadServiceUrl = workloadServiceUrl;
    }

    @Override
    public void sendEvent(TrainerWorkloadEventDto event) {
        try {
            String jwtToken = jwtService.generateTokenForService();
            String transactionId = MDC.get("transactionId");
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(jwtToken);
            headers.set("X-Transaction-Id", transactionId);
            HttpEntity<TrainerWorkloadEventDto> request = new HttpEntity<>(event, headers);
            restTemplate.postForEntity(workloadServiceUrl, request, Void.class);
            log.info("Workload event sent: {}", event);
        } catch (Exception ex) {
            log.error("Failed to send workload event: {}", event, ex);
        }
    }
}
