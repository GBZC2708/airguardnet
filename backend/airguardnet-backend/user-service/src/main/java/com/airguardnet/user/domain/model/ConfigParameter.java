package com.airguardnet.user.domain.model;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class ConfigParameter {
    private Long id;
    private String key;
    private String value;
    private Instant createdAt;
    private Instant updatedAt;
}
