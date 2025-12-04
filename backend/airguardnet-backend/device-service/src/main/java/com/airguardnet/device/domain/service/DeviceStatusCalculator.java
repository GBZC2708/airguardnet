package com.airguardnet.device.domain.service;

public class DeviceStatusCalculator {

    public String calculateStatus(int riskIndex) {
        if (riskIndex <= 25) return "ACTIVE";
        if (riskIndex <= 70) return "WARNING";
        return "CRITICAL";
    }
}
