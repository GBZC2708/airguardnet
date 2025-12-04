package com.airguardnet.user.domain.service;

import com.airguardnet.user.domain.model.SystemLog;
import com.airguardnet.user.domain.repository.SystemLogRepositoryPort;

import java.time.Instant;

public class SystemLogService {

    private final SystemLogRepositoryPort systemLogRepositoryPort;

    public SystemLogService(SystemLogRepositoryPort systemLogRepositoryPort) {
        this.systemLogRepositoryPort = systemLogRepositoryPort;
    }

    public void logError(String source, String message) {
        SystemLog log = SystemLog.builder()
                .type("ERROR")
                .source(source)
                .message(message)
                .createdAt(Instant.now())
                .build();
        systemLogRepositoryPort.save(log);
    }
}
