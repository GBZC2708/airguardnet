package com.airguardnet.user.infrastructure.persistence;

import com.airguardnet.user.domain.model.VersionHistory;
import com.airguardnet.user.domain.repository.VersionHistoryRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class VersionHistoryRepositoryAdapter implements VersionHistoryRepositoryPort {
    private final VersionHistoryJpaRepository versionHistoryJpaRepository;

    @Override
    public List<VersionHistory> findAll() {
        return versionHistoryJpaRepository.findAll().stream().map(VersionHistoryEntity::toDomain).toList();
    }
}
