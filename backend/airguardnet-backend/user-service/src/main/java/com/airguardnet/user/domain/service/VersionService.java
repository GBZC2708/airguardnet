package com.airguardnet.user.domain.service;

import com.airguardnet.user.domain.model.VersionHistory;
import com.airguardnet.user.domain.repository.VersionHistoryRepositoryPort;

import java.time.Instant;

public class VersionService {

    private final VersionHistoryRepositoryPort versionHistoryRepositoryPort;

    public VersionService(VersionHistoryRepositoryPort versionHistoryRepositoryPort) {
        this.versionHistoryRepositoryPort = versionHistoryRepositoryPort;
    }

    public VersionHistory registerVersion(String version, String description) {
        VersionHistory history = VersionHistory.builder()
                .versionNumber(version)
                .description(description)
                .releasedAt(Instant.now())
                .build();
        return versionHistoryRepositoryPort.save(history);
    }

    public VersionHistory getLatest() {
        return versionHistoryRepositoryPort.findTopByOrderByReleasedAtDesc()
                .orElseThrow();
    }
}
