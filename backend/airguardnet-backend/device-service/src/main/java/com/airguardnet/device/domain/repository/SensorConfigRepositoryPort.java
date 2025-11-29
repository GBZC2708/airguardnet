package com.airguardnet.device.domain.repository;

import com.airguardnet.device.domain.model.SensorConfig;

import java.util.List;
import java.util.Optional;

public interface SensorConfigRepositoryPort {
    Optional<SensorConfig> findBySensorType(String sensorType);

    List<SensorConfig> findAll();

    Optional<SensorConfig> findById(Long id);

    SensorConfig save(SensorConfig sensorConfig);
}
