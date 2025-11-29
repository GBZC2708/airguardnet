package com.airguardnet.user.infrastructure.persistence;

import com.airguardnet.user.domain.model.ConfigChangeLog;
import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

@Entity
@Table(name = "config_change_logs")
@Data
public class ConfigChangeLogEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String parameterKey;
    @Column(length = 2000)
    private String oldValue;
    @Column(length = 2000)
    private String newValue;
    private Long changedById;
    private Instant changedAt;

    public ConfigChangeLog toDomain() {
        return ConfigChangeLog.builder()
                .id(id)
                .parameterKey(parameterKey)
                .oldValue(oldValue)
                .newValue(newValue)
                .changedById(changedById)
                .changedAt(changedAt)
                .build();
    }
}
