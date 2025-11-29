package com.airguardnet.user.infrastructure.persistence;

import com.airguardnet.user.domain.model.AccessLog;
import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

@Entity
@Table(name = "access_logs")
@Data
public class AccessLogEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private String action;
    private String ipAddress;
    private Instant createdAt;

    public static AccessLogEntity fromDomain(AccessLog log) {
        AccessLogEntity entity = new AccessLogEntity();
        entity.setId(log.getId());
        entity.setUserId(log.getUserId());
        entity.setAction(log.getAction());
        entity.setIpAddress(log.getIpAddress());
        entity.setCreatedAt(log.getCreatedAt());
        return entity;
    }

    public AccessLog toDomain() {
        return AccessLog.builder()
                .id(id)
                .userId(userId)
                .action(action)
                .ipAddress(ipAddress)
                .createdAt(createdAt)
                .build();
    }
}
