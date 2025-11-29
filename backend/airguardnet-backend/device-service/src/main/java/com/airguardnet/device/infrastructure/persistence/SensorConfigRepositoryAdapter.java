package com.airguardnet.device.infrastructure.persistence;

import com.airguardnet.device.domain.model.SensorConfig;
import com.airguardnet.device.domain.repository.SensorConfigRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SensorConfigRepositoryAdapter implements SensorConfigRepositoryPort {
    private final SensorConfigJpaRepository sensorConfigJpaRepository;

    @Override
    public Optional<SensorConfig> findBySensorType(String sensorType) {
        return sensorConfigJpaRepository.findBySensorType(sensorType).map(SensorConfigEntity::toDomain);
    }

    @Override
    public List<SensorConfig> findAll() {
        return sensorConfigJpaRepository.findAll()
                .stream()
                .map(SensorConfigEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<SensorConfig> findById(Long id) {
        return sensorConfigJpaRepository.findById(id).map(SensorConfigEntity::toDomain);
    }

    @Override
    public SensorConfig save(SensorConfig sensorConfig) {
        SensorConfigEntity entity = new SensorConfigEntity();
        entity.setId(sensorConfig.getId());
        entity.setSensorType(sensorConfig.getSensorType());
        entity.setRecommendedMax(sensorConfig.getRecommendedMax());
        entity.setCriticalThreshold(sensorConfig.getCriticalThreshold());
        entity.setUnit(sensorConfig.getUnit());
        entity.setCreatedById(sensorConfig.getCreatedById());
        entity.setCreatedAt(sensorConfig.getCreatedAt());
        entity.setUpdatedAt(sensorConfig.getUpdatedAt());

        return sensorConfigJpaRepository.save(entity).toDomain();
    }
}
