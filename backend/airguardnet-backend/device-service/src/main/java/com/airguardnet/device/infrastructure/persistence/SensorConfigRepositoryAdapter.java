package com.airguardnet.device.infrastructure.persistence;

import com.airguardnet.device.domain.model.SensorConfig;
import com.airguardnet.device.domain.repository.SensorConfigRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SensorConfigRepositoryAdapter implements SensorConfigRepositoryPort {
    private final SensorConfigJpaRepository sensorConfigJpaRepository;

    @Override
    public Optional<SensorConfig> findBySensorType(String sensorType) {
        return sensorConfigJpaRepository.findBySensorType(sensorType).map(SensorConfigEntity::toDomain);
    }
}
