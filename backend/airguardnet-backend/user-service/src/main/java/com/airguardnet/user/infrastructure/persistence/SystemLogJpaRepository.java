package com.airguardnet.user.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SystemLogJpaRepository extends JpaRepository<SystemLogEntity, Long> {
    List<SystemLogEntity> findAllByOrderByCreatedAtDesc();
}
