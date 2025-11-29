package com.airguardnet.device.application.usecase;

import com.airguardnet.device.domain.model.Alert;

public class IngestReadingResult {
    private Long readingId;
    private Long deviceId;
    private int riskIndex;
    private int airQualityPercent;
    private String deviceStatus;
    private Alert createdAlert;

    public IngestReadingResult(Long readingId, Long deviceId, int riskIndex, int airQualityPercent, String deviceStatus, Alert createdAlert) {
        this.readingId = readingId;
        this.deviceId = deviceId;
        this.riskIndex = riskIndex;
        this.airQualityPercent = airQualityPercent;
        this.deviceStatus = deviceStatus;
        this.createdAlert = createdAlert;
    }

    public Long getReadingId() { return readingId; }
    public Long getDeviceId() { return deviceId; }
    public int getRiskIndex() { return riskIndex; }
    public int getAirQualityPercent() { return airQualityPercent; }
    public String getDeviceStatus() { return deviceStatus; }
    public Alert getCreatedAlert() { return createdAlert; }
}
