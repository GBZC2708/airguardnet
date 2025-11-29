package com.airguardnet.device.infrastructure.web.dto;

import com.airguardnet.device.domain.model.SensorConfig;
import com.airguardnet.device.infrastructure.persistence.SensorConfigEntity;

public class SensorConfigDTO {
    private Long id;
    private String sensorType;
    private Double recommendedMax;
    private Double criticalThreshold;
    private String unit;

    public static SensorConfigDTO fromEntity(SensorConfigEntity entity) {
        SensorConfigDTO dto = new SensorConfigDTO();
        dto.id = entity.getId();
        dto.sensorType = entity.getSensorType();
        dto.recommendedMax = entity.getRecommendedMax();
        dto.criticalThreshold = entity.getCriticalThreshold();
        dto.unit = entity.getUnit();
        return dto;
    }

    public static SensorConfigDTO fromDomain(SensorConfig config) {
        SensorConfigDTO dto = new SensorConfigDTO();
        dto.id = config.getId();
        dto.sensorType = config.getSensorType();
        dto.recommendedMax = config.getRecommendedMax();
        dto.criticalThreshold = config.getCriticalThreshold();
        dto.unit = config.getUnit();
        return dto;
    }

    public Long getId() {
        return id;
    }

    public String getSensorType() {
        return sensorType;
    }

    public Double getRecommendedMax() {
        return recommendedMax;
    }

    public Double getCriticalThreshold() {
        return criticalThreshold;
    }

    public String getUnit() {
        return unit;
    }
}
