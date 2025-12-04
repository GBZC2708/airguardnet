package com.airguardnet.device.domain.service;

import com.airguardnet.device.domain.model.Alert;
import com.airguardnet.device.domain.model.Device;
import com.airguardnet.device.domain.model.Reading;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class AlertFactoryTest {

    private final AlertFactory factory = new AlertFactory();

    @Test
    // Nro 26: Crear alerta CRITICAL cuando pm25 > critical_threshold
    void buildAlert_pmAboveCritical_createsCritical() {
        Device device = Device.builder().id(1L).build();
        Reading reading = Reading.builder().id(10L).pm25(90.0).build();

        Optional<Alert> opt = factory.buildAlert(device, reading, 35, 75);

        assertTrue(opt.isPresent());
        assertEquals("CRITICAL", opt.get().getSeverity());
        assertEquals("PENDING", opt.get().getStatus());
        assertEquals(device.getId(), opt.get().getDeviceId());
        assertEquals(reading.getId(), opt.get().getReadingId());
    }

    @Test
    // Nro 27: Crear alerta HIGH cuando pm25 entre recommended y critical
    void buildAlert_pmBetweenRecommendedAndCritical_createsHigh() {
        Device device = Device.builder().id(1L).build();
        Reading reading = Reading.builder().id(10L).pm25(50.0).build();

        Optional<Alert> opt = factory.buildAlert(device, reading, 35, 75);

        assertTrue(opt.isPresent());
        assertEquals("HIGH", opt.get().getSeverity());
    }

    @Test
    // Nro 28: No crear alerta cuando pm25 <= recommended_max
    void buildAlert_safePm_doesNotCreateAlert() {
        Device device = Device.builder().id(1L).build();
        Reading reading = Reading.builder().id(10L).pm25(20.0).build();

        Optional<Alert> opt = factory.buildAlert(device, reading, 35, 75);

        assertFalse(opt.isPresent());
    }
}
