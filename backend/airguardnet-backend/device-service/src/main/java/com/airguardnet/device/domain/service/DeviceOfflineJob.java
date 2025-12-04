package com.airguardnet.device.domain.service;

import com.airguardnet.device.domain.model.Device;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class DeviceOfflineJob {

    public boolean shouldMarkOffline(Device device, int offlineMinutes) {
        if (device == null || device.getLastCommunicationAt() == null) {
            return true;
        }
        Instant limit = Instant.now().minus(offlineMinutes, ChronoUnit.MINUTES);
        return device.getLastCommunicationAt().isBefore(limit);
    }
}
