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

    @Value("${airguardnet.services.user.base-url}")
    private String userServiceUrl;

    @Value("${airguardnet.services.device.base-url}")
    private String deviceServiceUrl;

    @Value("${airguardnet.services.notification.base-url}")
    private String notificationServiceUrl;

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

    // USERS - PLANS
    @GetMapping("/users/plans")
    public ResponseEntity<String> getPlans() {
        String url = userServiceUrl + "/users/plans";
        return restTemplate.getForEntity(url, String.class);
    }

    @GetMapping("/users/plans/{planId}/features")
    public ResponseEntity<String> getPlanFeatures(@PathVariable Long planId) {
        String url = userServiceUrl + "/users/plans/" + planId + "/features";
        return restTemplate.getForEntity(url, String.class);
    }

    // DEVICE REPORTS AND CONFIGS
    @GetMapping("/usage-reports")
    public ResponseEntity<String> getUsageReports() {
        String url = deviceServiceUrl + "/usage-reports";
        return restTemplate.getForEntity(url, String.class);
    }

    @GetMapping("/sensor-configs")
    public ResponseEntity<String> getSensorConfigs() {
        String url = deviceServiceUrl + "/sensor-configs";
        return restTemplate.getForEntity(url, String.class);
    }

    // CONFIG PARAMETERS
    @GetMapping("/config-parameters")
    public ResponseEntity<String> getConfigParameters() {
        String url = userServiceUrl + "/config-parameters";
        return restTemplate.getForEntity(url, String.class);
    }

    @PutMapping("/config-parameters/{key}")
    public ResponseEntity<String> upsertConfigParameter(@PathVariable String key, @RequestBody String body) {
        String url = userServiceUrl + "/config-parameters/" + key;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        return restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);
    }

    // LOGS
    @GetMapping("/access-logs")
    public ResponseEntity<String> getAccessLogs() {
        String url = userServiceUrl + "/access-logs";
        return restTemplate.getForEntity(url, String.class);
    }

    @GetMapping("/system-logs")
    public ResponseEntity<String> getSystemLogs() {
        String url = userServiceUrl + "/system-logs";
        return restTemplate.getForEntity(url, String.class);
    }

    // NOTIFICATIONS
    @GetMapping("/notifications/test")
    public ResponseEntity<String> notificationTest() {
        String url = notificationServiceUrl + "/notifications/test";
        return restTemplate.getForEntity(url, String.class);
    }
}
