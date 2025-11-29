package com.airguardnet.notification.application.usecase;

import com.airguardnet.notification.domain.model.Notification;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class SendNotificationUseCase {
    public Notification send(String channel, String title, String body) {
        return Notification.builder()
                .channel(channel)
                .title(title)
                .body(body)
                .createdAt(Instant.now())
                .build();
    }
}
