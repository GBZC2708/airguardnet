package com.airguardnet.device.infrastructure.persistence;

import com.airguardnet.device.domain.model.SensorConfig;
import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

@Entity
@Table(name = "sensor_configs")
@Data
public class SensorConfigEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String sensorType;
    private Double recommendedMax;
    private Double criticalThreshold;
    private String unit;
    private Long createdById;
    private Instant createdAt;
    private Instant updatedAt;

    public SensorConfig toDomain() {
        return SensorConfig.builder()
                .id(id)
                .sensorType(sensorType)
                .recommendedMax(recommendedMax)
                .criticalThreshold(criticalThreshold)
                .unit(unit)
                .createdById(createdById)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }
}
