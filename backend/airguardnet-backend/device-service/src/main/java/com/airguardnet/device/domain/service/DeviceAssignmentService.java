package com.airguardnet.device.domain.service;

import com.airguardnet.device.domain.model.Device;

public class DeviceAssignmentService {

    public void assignToUser(Device device, Long userId) {
        if (device != null) {
            device.setAssignedUserId(userId);
        }
    }
}
