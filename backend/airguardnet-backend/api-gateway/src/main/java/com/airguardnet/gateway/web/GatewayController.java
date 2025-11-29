package com.airguardnet.gateway.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
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

    // 1) LOGIN → user-service /auth/login
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody String body) {
        String url = userServiceUrl + "/auth/login";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        return restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
    }

    // 2) REGISTER → user-service /auth/register
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody String body) {
        String url = userServiceUrl + "/auth/register";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        return restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
    }

    // 3) USERS → user-service /users (por ahora sin JWT, igual que cuando llamas a 8081 directamente)
    @GetMapping("/users")
    public ResponseEntity<String> getUsers() {
        String url = userServiceUrl + "/users";
        return restTemplate.getForEntity(url, String.class);
    }

    // 4) INGEST → device-service /readings/ingest (lo dejamos listo)
    @PostMapping("/readings/ingest")
    public ResponseEntity<String> ingest(@RequestBody String body) {
        String url = deviceServiceUrl + "/readings/ingest";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        return restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
    }
}
