package com.airguardnet.device.domain.repository;

import com.airguardnet.device.domain.model.SensorConfig;

import java.util.Optional;

public interface SensorConfigRepositoryPort {
    Optional<SensorConfig> findBySensorType(String sensorType);
}
