package com.airguardnet.device.domain.service;

import com.airguardnet.device.domain.model.Device;
import com.airguardnet.device.domain.model.Reading;

public class ReadingValidator {

    public boolean belongsToDevice(Reading reading, Device device) {
        if (reading == null || device == null || reading.getDeviceId() == null || device.getId() == null) {
            return false;
        }
        return reading.getDeviceId().equals(device.getId());
    }
}
