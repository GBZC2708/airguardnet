package com.airguardnet.user.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanJpaRepository extends JpaRepository<PlanEntity, Long> {
}
