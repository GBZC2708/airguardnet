package com.airguardnet.device.infrastructure.persistence;

import com.airguardnet.device.domain.model.Device;
import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

@Entity
@Table(name = "devices")
@Data
public class DeviceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    public static DeviceEntity fromDomain(Device device) {
        DeviceEntity entity = new DeviceEntity();
        entity.setId(device.getId());
        entity.setDeviceUid(device.getDeviceUid());
        entity.setName(device.getName());
        entity.setStatus(device.getStatus());
        entity.setAssignedUserId(device.getAssignedUserId());
        entity.setLastCommunicationAt(device.getLastCommunicationAt());
        entity.setLastBatteryLevel(device.getLastBatteryLevel());
        entity.setCurrentFirmwareId(device.getCurrentFirmwareId());
        entity.setCreatedAt(device.getCreatedAt());
        entity.setUpdatedAt(device.getUpdatedAt());
        return entity;
    }

    public Device toDomain() {
        return Device.builder()
                .id(id)
                .deviceUid(deviceUid)
                .name(name)
                .status(status)
                .assignedUserId(assignedUserId)
                .lastCommunicationAt(lastCommunicationAt)
                .lastBatteryLevel(lastBatteryLevel)
                .currentFirmwareId(currentFirmwareId)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }
}
