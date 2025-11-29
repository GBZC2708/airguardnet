package com.airguardnet.device.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UsageReportJpaRepository extends JpaRepository<UsageReportEntity, Long> {
    List<UsageReportEntity> findAllByOrderByGeneratedAtDesc();
}
