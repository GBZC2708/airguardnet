package com.airguardnet.user.domain.repository;

import com.airguardnet.user.domain.model.VersionHistory;

import java.util.List;

public interface VersionHistoryRepositoryPort {
    List<VersionHistory> findAll();
}
