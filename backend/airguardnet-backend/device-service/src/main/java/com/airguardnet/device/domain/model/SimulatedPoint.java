package com.airguardnet.device.domain.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SimulatedPoint {
    private int airQualityPercent;
    private double pm25;
}
