package com.airguardnet.user.domain.model;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class ConfigChangeLog {
    private Long id;
    private String parameterKey;
    private String oldValue;
    private String newValue;
    private Long changedById;
    private Instant changedAt;
}
