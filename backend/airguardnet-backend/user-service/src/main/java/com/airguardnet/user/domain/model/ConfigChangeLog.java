package com.airguardnet.user.domain.model;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class ConfigChangeLog {
    private Long id;
    private String key;
    private String oldValue;
    private String newValue;
    private User changedBy;
    private Instant changedAt;
}
