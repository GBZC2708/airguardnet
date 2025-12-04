package com.airguardnet.user.domain.service;

import com.airguardnet.user.domain.model.Plan;
import com.airguardnet.user.domain.model.PlanFeature;

import java.util.List;

public class PlanFeatureService {

    public boolean hasFeature(Plan plan, List<PlanFeature> features, String featureKey) {
        Long planId = plan != null ? plan.getId() : null;
        return features.stream()
                .filter(f -> featureKey.equals(f.getFeatureKey()))
                .anyMatch(f -> (planId == null || planId.equals(f.getPlanId())) && f.isEnabled());
    }
}
