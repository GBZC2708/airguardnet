package com.airguardnet.device.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReadingJpaRepository extends JpaRepository<ReadingEntity, Long> {
}
