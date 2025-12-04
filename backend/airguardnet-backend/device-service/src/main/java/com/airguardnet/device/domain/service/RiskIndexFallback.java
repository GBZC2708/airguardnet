package com.airguardnet.device.domain.service;

public class RiskIndexFallback {

    public int calculateFallback(double pm25) {
        if (pm25 <= 35) return 10;
        if (pm25 <= 75) return 50;
        if (pm25 <= 150) return 80;
        return 95;
    }
}
