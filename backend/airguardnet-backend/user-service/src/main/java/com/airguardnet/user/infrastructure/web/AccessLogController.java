package com.airguardnet.user.infrastructure.web;

import com.airguardnet.common.web.ApiResponse;
import com.airguardnet.user.application.usecase.ListAccessLogsUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/access-logs")
public class AccessLogController {

    private final ListAccessLogsUseCase listAccessLogsUseCase;

    public AccessLogController(ListAccessLogsUseCase listAccessLogsUseCase) {
        this.listAccessLogsUseCase = listAccessLogsUseCase;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AccessLogDTO>>> listAll() {
        List<AccessLogDTO> logs = listAccessLogsUseCase.execute()
                .stream()
                .map(AccessLogDTO::fromDomain)
                .collect(Collectors.toList());

        ApiResponse<List<AccessLogDTO>> response = ApiResponse.success(logs);
        return ResponseEntity.ok(response);
    }
}
