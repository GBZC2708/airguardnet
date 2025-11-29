package com.airguardnet.user.application.usecase;

import com.airguardnet.user.domain.model.PlanFeature;
import com.airguardnet.user.domain.repository.PlanFeatureRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListPlanFeaturesUseCase {
    private final PlanFeatureRepositoryPort planFeatureRepositoryPort;

    public List<PlanFeature> execute(Long planId) {
        return planFeatureRepositoryPort.findByPlanId(planId);
    }
}
