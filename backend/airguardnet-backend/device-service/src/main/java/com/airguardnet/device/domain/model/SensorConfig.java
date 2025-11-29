package com.airguardnet.device.domain.model;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class SensorConfig {
    private Long id;
    private String sensorType;
    private Double recommendedMax;
    private Double criticalThreshold;
    private String unit;
    private Long createdById;
    private Instant createdAt;
    private Instant updatedAt;
}
