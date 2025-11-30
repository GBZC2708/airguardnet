package com.airguardnet.device.infrastructure.web;

import com.airguardnet.common.web.ApiResponse;
import com.airguardnet.device.application.usecase.ListAlertsUseCase;
import com.airguardnet.device.application.usecase.UpdateAlertStatusUseCase;
import com.airguardnet.device.domain.model.Alert;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/alerts")
public class AlertController {

    private final ListAlertsUseCase listAlertsUseCase;
    private final UpdateAlertStatusUseCase updateAlertStatusUseCase;

    public AlertController(ListAlertsUseCase listAlertsUseCase,
                           UpdateAlertStatusUseCase updateAlertStatusUseCase) {
        this.listAlertsUseCase = listAlertsUseCase;
        this.updateAlertStatusUseCase = updateAlertStatusUseCase;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Alert>>> listAlerts(@RequestParam(value = "limit", required = false) Integer limit,
                                                               @RequestParam(value = "status", required = false) String status,
                                                               @RequestParam(value = "severity", required = false) String severity) {
        List<Alert> alerts = listAlertsUseCase.execute(limit, status, severity);
        return ResponseEntity.ok(ApiResponse.success(alerts));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<Alert>> updateStatus(@PathVariable Long id,
                                                           @RequestBody AlertStatusUpdateRequest request) {
        return updateAlertStatusUseCase.execute(id, request.getStatus())
                .map(alert -> ResponseEntity.ok(ApiResponse.success(alert)))
                .orElseGet(() -> ResponseEntity.status(404).body(ApiResponse.error("Alert not found")));
    }
}
