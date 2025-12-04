package com.airguardnet.device.domain.service;

public class AirQualityCalculator {

    public int calculateAirQualityPercent(int riskIndex) {
        int value = 100 - riskIndex;
        if (value < 0) return 0;
        if (value > 100) return 100;
        return value;
    }
}
