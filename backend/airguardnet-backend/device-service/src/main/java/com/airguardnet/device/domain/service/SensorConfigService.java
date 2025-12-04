package com.airguardnet.device.domain.service;

import com.airguardnet.device.domain.model.SensorConfig;
import com.airguardnet.device.domain.repository.SensorConfigRepositoryPort;

public class SensorConfigService {

    private final SensorConfigRepositoryPort sensorConfigRepositoryPort;

    public SensorConfigService(SensorConfigRepositoryPort sensorConfigRepositoryPort) {
        this.sensorConfigRepositoryPort = sensorConfigRepositoryPort;
    }

    public SensorConfig create(String sensorType, double recommended, double critical, Object createdBy) {

        // Usamos el builder de dominio en lugar de new SensorConfig()
        SensorConfig.SensorConfigBuilder builder = SensorConfig.builder()
                .sensorType(sensorType)
                .recommendedMax(recommended)
                .criticalThreshold(critical)
                .unit("µg/m³");

        if (createdBy instanceof Number number) {
            builder.createdById(number.longValue());
        }

        SensorConfig config = builder.build();

        return sensorConfigRepositoryPort.save(config);
    }
}
