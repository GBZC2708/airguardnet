package com.airguardnet.user.infrastructure.web;

import com.airguardnet.user.domain.model.PlanFeature;

public class PlanFeatureDTO {
    private Long id;
    private String featureKey;
    private boolean enabled;

    public static PlanFeatureDTO fromDomain(PlanFeature feature) {
        PlanFeatureDTO dto = new PlanFeatureDTO();
        dto.id = feature.getId();
        dto.featureKey = feature.getFeatureKey();
        dto.enabled = feature.isEnabled();
        return dto;
    }

    public Long getId() { return id; }
    public String getFeatureKey() { return featureKey; }
    public boolean isEnabled() { return enabled; }
}
