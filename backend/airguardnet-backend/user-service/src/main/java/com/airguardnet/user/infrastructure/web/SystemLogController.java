package com.airguardnet.user.infrastructure.web;

import com.airguardnet.common.web.ApiResponse;
import com.airguardnet.user.application.usecase.ListSystemLogsUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/system-logs")
public class SystemLogController {

    private final ListSystemLogsUseCase listSystemLogsUseCase;

    public SystemLogController(ListSystemLogsUseCase listSystemLogsUseCase) {
        this.listSystemLogsUseCase = listSystemLogsUseCase;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<SystemLogDTO>>> listAll() {
        List<SystemLogDTO> logs = listSystemLogsUseCase.execute()
                .stream()
                .map(SystemLogDTO::fromDomain)
                .collect(Collectors.toList());

        ApiResponse<List<SystemLogDTO>> response = ApiResponse.success(logs);
        return ResponseEntity.ok(response);
    }
}
