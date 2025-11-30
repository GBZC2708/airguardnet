package com.airguardnet.device.infrastructure.persistence;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlertJpaRepository extends JpaRepository<AlertEntity, Long> {
    List<AlertEntity> findByDeviceIdOrderByCreatedAtDesc(Long deviceId);
    List<AlertEntity> findByStatusOrderByCreatedAtDesc(String status, Pageable pageable);
    List<AlertEntity> findBySeverityOrderByCreatedAtDesc(String severity, Pageable pageable);
    List<AlertEntity> findByStatusAndSeverityOrderByCreatedAtDesc(String status, String severity, Pageable pageable);
    List<AlertEntity> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
