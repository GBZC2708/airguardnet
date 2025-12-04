package com.airguardnet.user.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VersionHistoryJpaRepository extends JpaRepository<VersionHistoryEntity, Long> {
    Optional<VersionHistoryEntity> findTopByOrderByReleasedAtDesc();
}
