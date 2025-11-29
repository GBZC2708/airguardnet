package com.airguardnet.device.infrastructure.web;

import jakarta.validation.constraints.NotBlank;

public class ReadingIngestRequest {
    @NotBlank
    private String deviceUid;
    private Double pm1;
    private Double pm25;
    private Double pm10;
    private Double batteryLevel;
    private String timestamp;

    public String getDeviceUid() { return deviceUid; }
    public void setDeviceUid(String deviceUid) { this.deviceUid = deviceUid; }
    public Double getPm1() { return pm1; }
    public void setPm1(Double pm1) { this.pm1 = pm1; }
    public Double getPm25() { return pm25; }
    public void setPm25(Double pm25) { this.pm25 = pm25; }
    public Double getPm10() { return pm10; }
    public void setPm10(Double pm10) { this.pm10 = pm10; }
    public Double getBatteryLevel() { return batteryLevel; }
    public void setBatteryLevel(Double batteryLevel) { this.batteryLevel = batteryLevel; }
    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
}
