package com.airguardnet.user.infrastructure.persistence;

import com.airguardnet.user.domain.model.ConfigParameter;
import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

@Entity
@Table(name = "config_parameters")
@Data
public class ConfigParameterEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "key")
    private String key;
    @Column(length = 2000)
    private String value;
    private Instant createdAt;
    private Instant updatedAt;

    public ConfigParameter toDomain() {
        return ConfigParameter.builder()
                .id(id)
                .key(key)
                .value(value)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }
}
