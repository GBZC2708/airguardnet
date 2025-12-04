package com.airguardnet.device.domain.service;

import com.airguardnet.device.domain.model.SensorConfig;
import com.airguardnet.device.domain.repository.SensorConfigRepositoryPort;

public class SensorConfigService {

    private final SensorConfigRepositoryPort sensorConfigRepositoryPort;

    public SensorConfigService(SensorConfigRepositoryPort sensorConfigRepositoryPort) {
        this.sensorConfigRepositoryPort = sensorConfigRepositoryPort;
    }

    public SensorConfig create(String sensorType, double recommended, double critical, Object createdBy) {
        SensorConfig config = new SensorConfig();
        config.setSensorType(sensorType);
        config.setRecommendedMax(recommended);
        config.setCriticalThreshold(critical);
        config.setUnit("µg/m³");
        if (createdBy instanceof Number number) {
            config.setCreatedById(number.longValue());
        }
        return sensorConfigRepositoryPort.save(config);
    }
}
