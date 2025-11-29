package com.airguardnet.device.domain.model;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class Device {
    private Long id;
    private String deviceUid;
    private String name;
    private String status;
    private Long assignedUserId;
    private Instant lastCommunicationAt;
    private Double lastBatteryLevel;
    private Long currentFirmwareId;
    private Instant createdAt;
    private Instant updatedAt;
}
