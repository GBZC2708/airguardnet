package com.airguardnet.user.infrastructure.persistence;

import com.airguardnet.user.domain.model.SystemLog;
import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

@Entity
@Table(name = "system_logs")
@Data
public class SystemLogEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String type;
    private String source;
    @Column(length = 2000)
    private String message;
    private Instant createdAt;

    public static SystemLogEntity fromDomain(SystemLog log) {
        SystemLogEntity entity = new SystemLogEntity();
        entity.setId(log.getId());
        entity.setType(log.getType());
        entity.setSource(log.getSource());
        entity.setMessage(log.getMessage());
        entity.setCreatedAt(log.getCreatedAt());
        return entity;
    }

    public SystemLog toDomain() {
        return SystemLog.builder()
                .id(id)
                .type(type)
                .source(source)
                .message(message)
                .createdAt(createdAt)
                .build();
    }
}
