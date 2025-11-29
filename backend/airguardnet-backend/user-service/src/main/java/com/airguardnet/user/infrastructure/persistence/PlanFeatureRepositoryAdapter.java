package com.airguardnet.user.infrastructure.persistence;

import com.airguardnet.user.domain.model.PlanFeature;
import com.airguardnet.user.domain.repository.PlanFeatureRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PlanFeatureRepositoryAdapter implements PlanFeatureRepositoryPort {
    private final PlanFeatureJpaRepository planFeatureJpaRepository;

    @Override
    public List<PlanFeature> findByPlanId(Long planId) {
        return planFeatureJpaRepository.findByPlanId(planId).stream().map(PlanFeatureEntity::toDomain).toList();
    }
}
