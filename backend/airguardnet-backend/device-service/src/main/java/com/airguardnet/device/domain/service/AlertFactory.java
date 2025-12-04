package com.airguardnet.device.domain.service;

import com.airguardnet.device.domain.model.Alert;
import com.airguardnet.device.domain.model.Device;
import com.airguardnet.device.domain.model.Reading;

import java.util.Optional;

public class AlertFactory {

    public Optional<Alert> buildAlert(Device device, Reading reading, double recommendedMax, double criticalThreshold) {
        if (reading == null || reading.getPm25() == null) {
            return Optional.empty();
        }
        double pm25 = reading.getPm25();
        if (pm25 > criticalThreshold) {
            return Optional.of(Alert.builder()
                    .deviceId(device != null ? device.getId() : null)
                    .readingId(reading.getId())
                    .severity("CRITICAL")
                    .status("PENDING")
                    .build());
        }
        if (pm25 > recommendedMax) {
            return Optional.of(Alert.builder()
                    .deviceId(device != null ? device.getId() : null)
                    .readingId(reading.getId())
                    .severity("HIGH")
                    .status("PENDING")
                    .build());
        }
        return Optional.empty();
    }
}
