// Cobertura matriz Nro 29–31
package com.airguardnet.device.domain.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DeviceStatusCalculatorTest {

    private final DeviceStatusCalculator calculator = new DeviceStatusCalculator();

    @Test
    // Nro 29: Actualizar estado de dispositivo a ACTIVE con risk bajo
    void calculateStatus_lowRisk_active() {
        assertEquals("ACTIVE", calculator.calculateStatus(15));
    }

    @Test
    // Nro 30: Actualizar estado de dispositivo a WARNING con risk medio
    void calculateStatus_midRisk_warning() {
        assertEquals("WARNING", calculator.calculateStatus(50));
    }

    @Test
    // Nro 31: Actualizar estado de dispositivo a CRITICAL con risk alto
    void calculateStatus_highRisk_critical() {
        assertEquals("CRITICAL", calculator.calculateStatus(90));
    }
}
