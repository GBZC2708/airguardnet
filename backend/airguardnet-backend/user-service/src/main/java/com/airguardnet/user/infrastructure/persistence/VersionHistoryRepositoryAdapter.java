package com.airguardnet.user.infrastructure.persistence;

import com.airguardnet.user.domain.model.VersionHistory;
import com.airguardnet.user.domain.repository.VersionHistoryRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class VersionHistoryRepositoryAdapter implements VersionHistoryRepositoryPort {
    private final VersionHistoryJpaRepository versionHistoryJpaRepository;

    @Override
    public List<VersionHistory> findAll() {
        return versionHistoryJpaRepository.findAll().stream().map(VersionHistoryEntity::toDomain).toList();
    }

    @Override
    public VersionHistory save(VersionHistory versionHistory) {
        VersionHistoryEntity entity = new VersionHistoryEntity();
        entity.setId(versionHistory.getId());
        entity.setVersionNumber(versionHistory.getVersionNumber());
        entity.setDescription(versionHistory.getDescription());
        entity.setReleasedAt(versionHistory.getReleasedAt());
        return versionHistoryJpaRepository.save(entity).toDomain();
    }

    @Override
    public Optional<VersionHistory> findTopByOrderByReleasedAtDesc() {
        return versionHistoryJpaRepository.findTopByOrderByReleasedAtDesc().map(VersionHistoryEntity::toDomain);
    }
}
