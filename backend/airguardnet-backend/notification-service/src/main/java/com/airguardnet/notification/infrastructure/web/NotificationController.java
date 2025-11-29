package com.airguardnet.notification.infrastructure.web;

import com.airguardnet.common.web.ApiResponse;
import com.airguardnet.notification.application.usecase.SendNotificationUseCase;
import com.airguardnet.notification.domain.model.Notification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notifications")
public class NotificationController {
    private final SendNotificationUseCase sendNotificationUseCase;

    public NotificationController(SendNotificationUseCase sendNotificationUseCase) {
        this.sendNotificationUseCase = sendNotificationUseCase;
    }

    @GetMapping("/test")
    public ResponseEntity<ApiResponse<Notification>> test() {
        Notification notification = sendNotificationUseCase.send("PUSH", "Test", "OK");
        return ResponseEntity.ok(ApiResponse.success(notification));
    }
}
