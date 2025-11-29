package com.airguardnet.device.infrastructure.web;

import com.airguardnet.common.web.ApiResponse;
import com.airguardnet.device.application.usecase.ListSensorConfigsUseCase;
import com.airguardnet.device.application.usecase.UpdateSensorConfigUseCase;
import com.airguardnet.device.infrastructure.web.dto.SensorConfigDTO;
import com.airguardnet.device.infrastructure.web.dto.SensorConfigUpdateRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/sensor-configs")
public class SensorConfigController {

    private final ListSensorConfigsUseCase listSensorConfigsUseCase;
    private final UpdateSensorConfigUseCase updateSensorConfigUseCase;

    public SensorConfigController(ListSensorConfigsUseCase listSensorConfigsUseCase,
                                  UpdateSensorConfigUseCase updateSensorConfigUseCase) {
        this.listSensorConfigsUseCase = listSensorConfigsUseCase;
        this.updateSensorConfigUseCase = updateSensorConfigUseCase;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<SensorConfigDTO>>> listAll() {
        List<SensorConfigDTO> configs = listSensorConfigsUseCase.execute()
                .stream()
                .map(SensorConfigDTO::fromDomain)
                .collect(Collectors.toList());

        return ResponseEntity.ok(ApiResponse.success(configs));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SensorConfigDTO>> update(@PathVariable Long id,
                                                                @RequestBody SensorConfigUpdateRequest request) {
        if (request.getRecommendedMax() == null || request.getCriticalThreshold() == null) {
            throw new IllegalArgumentException("recommendedMax and criticalThreshold are required");
        }
        SensorConfigDTO dto = SensorConfigDTO.fromDomain(
                updateSensorConfigUseCase.execute(id, request.getRecommendedMax(), request.getCriticalThreshold(), request.getUnit())
        );
        return ResponseEntity.ok(ApiResponse.success(dto));
    }
}
