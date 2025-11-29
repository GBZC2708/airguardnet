package com.airguardnet.user.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccessLogJpaRepository extends JpaRepository<AccessLogEntity, Long> {
    List<AccessLogEntity> findAllByOrderByCreatedAtDesc();
}
