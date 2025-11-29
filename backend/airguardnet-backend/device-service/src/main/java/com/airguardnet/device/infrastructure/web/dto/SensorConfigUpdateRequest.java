package com.airguardnet.device.infrastructure.web.dto;

public class SensorConfigUpdateRequest {
    private Double recommendedMax;
    private Double criticalThreshold;
    private String unit;

    public Double getRecommendedMax() {
        return recommendedMax;
    }

    public void setRecommendedMax(Double recommendedMax) {
        this.recommendedMax = recommendedMax;
    }

    public Double getCriticalThreshold() {
        return criticalThreshold;
    }

    public void setCriticalThreshold(Double criticalThreshold) {
        this.criticalThreshold = criticalThreshold;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
