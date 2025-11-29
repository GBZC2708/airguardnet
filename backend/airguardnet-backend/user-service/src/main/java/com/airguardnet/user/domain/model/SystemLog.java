package com.airguardnet.user.domain.model;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class SystemLog {
    private Long id;
    private String type;
    private String source;
    private String message;
    private Instant createdAt;
}
