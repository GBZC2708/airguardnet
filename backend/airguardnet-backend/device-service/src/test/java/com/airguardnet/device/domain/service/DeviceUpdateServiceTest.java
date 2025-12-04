package com.airguardnet.device.domain.service;

import com.airguardnet.device.domain.model.Device;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DeviceUpdateServiceTest {

    @Test
    // Nro 35: Cambiar nombre de dispositivo
    void rename_updatesName() {
        Device device = Device.builder().name("Old").build();
        DeviceUpdateService service = new DeviceUpdateService();

        service.rename(device, "Pulsera Demo 1 - Zona A");

        assertEquals("Pulsera Demo 1 - Zona A", device.getName());
    }
}
