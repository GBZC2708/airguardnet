package com.airguardnet.device.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SensorConfigJpaRepository extends JpaRepository<SensorConfigEntity, Long> {
    Optional<SensorConfigEntity> findBySensorType(String sensorType);
}
