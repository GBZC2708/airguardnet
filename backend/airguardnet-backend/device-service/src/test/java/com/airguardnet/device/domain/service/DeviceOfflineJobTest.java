// Cobertura matriz Nro 32–33
package com.airguardnet.device.domain.service;

import com.airguardnet.device.domain.model.Device;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DeviceOfflineJobTest {

    private final DeviceOfflineJob job = new DeviceOfflineJob();

    @Test
    // Nro 32: Marcar device como OFFLINE cuando supera minutos sin comunicación
    void shouldMarkOffline_oldCommunication_returnsTrue() {
        Device device = Device.builder().lastCommunicationAt(Instant.now().minus(20, ChronoUnit.MINUTES)).build();
        assertTrue(job.shouldMarkOffline(device, 15));
    }

    @Test
    // Nro 33: No marcar OFFLINE si comunicación reciente
    void shouldMarkOffline_recentCommunication_returnsFalse() {
        Device device = Device.builder().lastCommunicationAt(Instant.now().minus(5, ChronoUnit.MINUTES)).build();
        assertFalse(job.shouldMarkOffline(device, 15));
    }
}
