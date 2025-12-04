// Cobertura matriz Nro 34
package com.airguardnet.device.domain.service;

import com.airguardnet.device.domain.model.Device;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DeviceAssignmentServiceTest {

    @Test
    // Nro 34: Asignar dispositivo a usuario técnico (adaptado a assignedUserId)
    void assignToUser_setsAssignedUserId() {
        Device device = Device.builder().build();
        DeviceAssignmentService service = new DeviceAssignmentService();

        service.assignToUser(device, 99L);

        assertEquals(99L, device.getAssignedUserId());
    }
}
