package com.airguardnet.device.domain.service;

import com.airguardnet.device.domain.model.Alert;
import com.airguardnet.device.domain.model.Reading;

public class AlertMessageFormatter {

    public String format(Alert alert) {
        Reading reading = alert.getReading();
        Double pm = reading != null ? reading.getPm25() : null;
        return String.format("PM2.5 en %.1f µg/m³ supera umbral", pm);
    }
}
