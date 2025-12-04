package com.airguardnet.device.domain.service;

import com.airguardnet.device.domain.model.Device;

public class DeviceUpdateService {

    public void rename(Device device, String newName) {
        if (device != null) {
            device.setName(newName);
        }
    }
}
