package com.airguardnet.user.domain.repository;

import com.airguardnet.user.domain.model.VersionHistory;

import java.util.List;
import java.util.Optional;

public interface VersionHistoryRepositoryPort {
    List<VersionHistory> findAll();

    VersionHistory save(VersionHistory versionHistory);

    Optional<VersionHistory> findTopByOrderByReleasedAtDesc();
}
