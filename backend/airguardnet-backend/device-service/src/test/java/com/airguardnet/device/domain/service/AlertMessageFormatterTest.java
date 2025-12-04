package com.airguardnet.device.domain.service;

import com.airguardnet.device.domain.model.Alert;
import com.airguardnet.device.domain.model.Reading;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class AlertMessageFormatterTest {

    private final AlertMessageFormatter formatter = new AlertMessageFormatter();

    @Test
    // Nro 56: Formatear mensaje de alerta para UI
    void format_includesPmValue() {
        Reading reading = Reading.builder().pm25(80.0).build();
        Alert alert = Alert.builder().reading(reading).build();

        String message = formatter.format(alert);

        assertTrue(message.contains("80.0"));
    }
}
