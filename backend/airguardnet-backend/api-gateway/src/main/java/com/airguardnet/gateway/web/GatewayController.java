package com.airguardnet.gateway.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api")
public class GatewayController {
    private final RestTemplate restTemplate;

    @Value("${services.user-service.url:http://localhost:8081}")
    private String userServiceUrl;

    @Value("${services.device-service.url:http://localhost:8082}")
    private String deviceServiceUrl;

    public GatewayController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody String body) {
        return restTemplate.postForEntity(userServiceUrl + "/auth/login", body, String.class);
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody String body) {
        return restTemplate.postForEntity(userServiceUrl + "/auth/register", body, String.class);
    }

    @PostMapping("/readings/ingest")
    public ResponseEntity<String> ingest(@RequestBody String body) {
        return restTemplate.postForEntity(deviceServiceUrl + "/readings/ingest", body, String.class);
    }
}
