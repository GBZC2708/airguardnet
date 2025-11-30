package com.airguardnet.device.infrastructure.persistence;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReadingJpaRepository extends JpaRepository<ReadingEntity, Long> {
    List<ReadingEntity> findByDeviceIdOrderByRecordedAtDesc(Long deviceId, Pageable pageable);
}
