package com.airguardnet.device.infrastructure.web.dto;

import com.airguardnet.device.infrastructure.persistence.UsageReportEntity;

import java.time.OffsetDateTime;

public class UsageReportDTO {
    private Long id;
    private OffsetDateTime generatedAt;
    private int totalUsers;
    private int totalDevices;
    private int totalReadings;
    private int totalAlerts;

    public static UsageReportDTO fromEntity(UsageReportEntity entity) {
        UsageReportDTO dto = new UsageReportDTO();
        dto.id = entity.getId();
        dto.generatedAt = entity.getGeneratedAt();
        dto.totalUsers = entity.getTotalUsers();
        dto.totalDevices = entity.getTotalDevices();
        dto.totalReadings = entity.getTotalReadings();
        dto.totalAlerts = entity.getTotalAlerts();
        return dto;
    }

    public Long getId() {
        return id;
    }

    public OffsetDateTime getGeneratedAt() {
        return generatedAt;
    }

    public int getTotalUsers() {
        return totalUsers;
    }

    public int getTotalDevices() {
        return totalDevices;
    }

    public int getTotalReadings() {
        return totalReadings;
    }

    public int getTotalAlerts() {
        return totalAlerts;
    }
}
