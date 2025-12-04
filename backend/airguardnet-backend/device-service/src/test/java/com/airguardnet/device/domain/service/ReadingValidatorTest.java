package com.airguardnet.device.domain.service;

import com.airguardnet.device.domain.model.Device;
import com.airguardnet.device.domain.model.Reading;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ReadingValidatorTest {

    private final ReadingValidator readingValidator = new ReadingValidator();

    @Test
    // Nro 58: Validar que lectura pertenece al dispositivo correcto
    void belongsToDevice_sameIds_true() {
        Device device = Device.builder().id(1L).build();
        Reading reading = Reading.builder().deviceId(1L).build();

        assertTrue(readingValidator.belongsToDevice(reading, device));
    }
}
