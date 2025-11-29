package com.airguardnet.device.application.usecase;

import lombok.Data;

@Data
public class IngestReadingCommand {
    private String deviceUid;
    private Double pm1;
    private Double pm25;
    private Double pm10;
    private Double batteryLevel;
    private String timestamp;
}
