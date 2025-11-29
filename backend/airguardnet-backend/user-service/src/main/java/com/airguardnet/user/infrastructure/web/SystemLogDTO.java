package com.airguardnet.user.infrastructure.web;

import com.airguardnet.user.domain.model.SystemLog;
import com.airguardnet.user.infrastructure.persistence.SystemLogEntity;

import java.time.Instant;

public class SystemLogDTO {
    private Long id;
    private String type;
    private String source;
    private String message;
    private Instant createdAt;

    public static SystemLogDTO fromEntity(SystemLogEntity entity) {
        SystemLogDTO dto = new SystemLogDTO();
        dto.id = entity.getId();
        dto.type = entity.getType();
        dto.source = entity.getSource();
        dto.message = entity.getMessage();
        dto.createdAt = entity.getCreatedAt();
        return dto;
    }

    public static SystemLogDTO fromDomain(SystemLog log) {
        SystemLogDTO dto = new SystemLogDTO();
        dto.id = log.getId();
        dto.type = log.getType();
        dto.source = log.getSource();
        dto.message = log.getMessage();
        dto.createdAt = log.getCreatedAt();
        return dto;
    }

    public Long getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getSource() {
        return source;
    }

    public String getMessage() {
        return message;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
