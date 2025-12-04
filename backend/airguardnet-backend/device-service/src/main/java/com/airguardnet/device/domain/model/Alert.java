package com.airguardnet.device.domain.model;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class Alert {
    private Long id;
    private Long deviceId;
    private Long readingId;
    private Reading reading;
    private String severity;
    private String status;
    private String message;
    private Instant createdAt;
    private Instant resolvedAt;
    private Long responsibleUserId;
}
