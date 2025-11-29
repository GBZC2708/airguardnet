package com.airguardnet.notification.domain.model;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class Notification {
    private Long id;
    private String channel;
    private String title;
    private String body;
    private Instant createdAt;
}
