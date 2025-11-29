package com.airguardnet.user.domain.repository;

import com.airguardnet.user.domain.model.PlanFeature;

import java.util.List;

public interface PlanFeatureRepositoryPort {
    List<PlanFeature> findByPlanId(Long planId);
}
