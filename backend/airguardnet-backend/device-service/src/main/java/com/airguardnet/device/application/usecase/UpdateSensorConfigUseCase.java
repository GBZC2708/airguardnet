package com.airguardnet.device.application.usecase;

import com.airguardnet.device.domain.model.SensorConfig;
import com.airguardnet.device.domain.repository.SensorConfigRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class UpdateSensorConfigUseCase {

    private final SensorConfigRepositoryPort sensorConfigRepositoryPort;

    public SensorConfig execute(Long id, Double recommendedMax, Double criticalThreshold, String unit) {
        SensorConfig sensorConfig = sensorConfigRepositoryPort.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Sensor configuration not found"));

        sensorConfig.setRecommendedMax(recommendedMax);
        sensorConfig.setCriticalThreshold(criticalThreshold);
        if (unit != null) {
            sensorConfig.setUnit(unit);
        }
        sensorConfig.setUpdatedAt(Instant.now());

        return sensorConfigRepositoryPort.save(sensorConfig);
    }
}
