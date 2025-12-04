package com.airguardnet.device.domain.service;

public class RiskIndexCalculator {

    public int calculate(double pm25, double recommendedMax, double criticalThreshold) {
        if (pm25 <= recommendedMax) {
            return (int) Math.round(pm25 / recommendedMax * 25);
        }
        if (pm25 < criticalThreshold) {
            double ratio = (pm25 - recommendedMax) / (criticalThreshold - recommendedMax);
            return 26 + (int) Math.round(ratio * (70 - 26));
        }
        double ratio = (pm25 - criticalThreshold) / (2000 - criticalThreshold);
        return 71 + (int) Math.round(Math.min(ratio, 1.0) * (100 - 71));
    }
}
