package com.airguardnet.user.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlanFeatureJpaRepository extends JpaRepository<PlanFeatureEntity, Long> {
    List<PlanFeatureEntity> findByPlanId(Long planId);
}
