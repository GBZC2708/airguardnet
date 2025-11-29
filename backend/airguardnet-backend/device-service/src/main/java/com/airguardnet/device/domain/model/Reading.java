package com.airguardnet.device.domain.model;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class Reading {
    private Long id;
    private Long deviceId;
    private Instant recordedAt;
    private Double pm1;
    private Double pm25;
    private Double pm10;
    private Double batteryLevel;
    private Integer riskIndex;
    private Integer airQualityPercent;
}
