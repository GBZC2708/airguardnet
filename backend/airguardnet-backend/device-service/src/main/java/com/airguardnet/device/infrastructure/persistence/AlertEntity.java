package com.airguardnet.device.infrastructure.persistence;

import com.airguardnet.device.domain.model.Alert;
import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

@Entity
@Table(name = "alerts")
@Data
public class AlertEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long deviceId;
    private Long readingId;
    private String severity;
    private String status;
    @Column(length = 2000)
    private String message;
    private Instant createdAt;
    private Instant resolvedAt;
    private Long responsibleUserId;

    public static AlertEntity fromDomain(Alert alert) {
        AlertEntity entity = new AlertEntity();
        entity.setId(alert.getId());
        entity.setDeviceId(alert.getDeviceId());
        entity.setReadingId(alert.getReadingId());
        entity.setSeverity(alert.getSeverity());
        entity.setStatus(alert.getStatus());
        entity.setMessage(alert.getMessage());
        entity.setCreatedAt(alert.getCreatedAt());
        entity.setResolvedAt(alert.getResolvedAt());
        entity.setResponsibleUserId(alert.getResponsibleUserId());
        return entity;
    }

    public Alert toDomain() {
        return Alert.builder()
                .id(id)
                .deviceId(deviceId)
                .readingId(readingId)
                .severity(severity)
                .status(status)
                .message(message)
                .createdAt(createdAt)
                .resolvedAt(resolvedAt)
                .responsibleUserId(responsibleUserId)
                .build();
    }
}
