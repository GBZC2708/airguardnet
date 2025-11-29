package com.airguardnet.device.infrastructure.web;

import com.airguardnet.common.web.ApiResponse;
import com.airguardnet.device.application.usecase.ListUsageReportsUseCase;
import com.airguardnet.device.infrastructure.web.dto.UsageReportDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/usage-reports")
public class UsageReportController {

    private final ListUsageReportsUseCase listUsageReportsUseCase;

    public UsageReportController(ListUsageReportsUseCase listUsageReportsUseCase) {
        this.listUsageReportsUseCase = listUsageReportsUseCase;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UsageReportDTO>>> listAll() {
        List<UsageReportDTO> reports = listUsageReportsUseCase.execute()
                .stream()
                .map(UsageReportDTO::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(ApiResponse.success(reports));
    }
}
