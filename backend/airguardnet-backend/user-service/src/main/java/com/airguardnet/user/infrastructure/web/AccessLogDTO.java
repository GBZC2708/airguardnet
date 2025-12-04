package com.airguardnet.user.infrastructure.web;

import com.airguardnet.user.domain.model.AccessLog;
import com.airguardnet.user.infrastructure.persistence.AccessLogEntity;

import java.time.Instant;

public class AccessLogDTO {
    private Long id;
    private Long userId;
    private String userEmail;
    private String action;
    private String ipAddress;
    private Instant createdAt;

    public static AccessLogDTO fromEntity(AccessLogEntity entity) {
        AccessLogDTO dto = new AccessLogDTO();
        dto.id = entity.getId();
        dto.userId = entity.getUserId();
        dto.action = entity.getAction();
        dto.ipAddress = entity.getIpAddress();
        dto.createdAt = entity.getCreatedAt();
        return dto;
    }

    public static AccessLogDTO fromDomain(AccessLog log) {
        AccessLogDTO dto = new AccessLogDTO();
        dto.id = log.getId();
        dto.userId = log.getUserId();
        if (log.getUser() != null) {
            dto.userEmail = log.getUser().getEmail();
        }
        dto.action = log.getAction();
        dto.ipAddress = log.getIpAddress();
        dto.createdAt = log.getCreatedAt();
        return dto;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getAction() {
        return action;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
