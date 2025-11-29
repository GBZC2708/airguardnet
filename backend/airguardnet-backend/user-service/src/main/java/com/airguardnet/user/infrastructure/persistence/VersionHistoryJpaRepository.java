package com.airguardnet.user.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VersionHistoryJpaRepository extends JpaRepository<VersionHistoryEntity, Long> {
}
