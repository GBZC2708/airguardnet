package com.airguardnet.user.domain.model;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class AccessLog {
    private Long id;
    private Long userId;
    private User user;
    private String action;
    private String ipAddress;
    private Instant createdAt;
}
