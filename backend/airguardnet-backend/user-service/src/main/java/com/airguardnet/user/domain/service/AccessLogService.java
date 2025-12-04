package com.airguardnet.user.domain.service;

import com.airguardnet.user.domain.model.AccessLog;
import com.airguardnet.user.domain.model.User;
import com.airguardnet.user.domain.repository.AccessLogRepositoryPort;

import java.time.Instant;

public class AccessLogService {

    private final AccessLogRepositoryPort accessLogRepositoryPort;

    public AccessLogService(AccessLogRepositoryPort accessLogRepositoryPort) {
        this.accessLogRepositoryPort = accessLogRepositoryPort;
    }

    public void logAccess(User user, String action, String ip) {
        AccessLog log = AccessLog.builder()
                .user(user)
                .userId(user != null ? user.getId() : null)
                .action(action)
                .ipAddress(ip)
                .createdAt(Instant.now())
                .build();
        accessLogRepositoryPort.save(log);
    }
}
