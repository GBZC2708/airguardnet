package com.airguardnet.device.domain.model;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class SensorNote {
    private Long id;
    private Device device;
    private String sensorType;
    private String noteText;
    private Long createdById;
    private Instant createdAt;
}
