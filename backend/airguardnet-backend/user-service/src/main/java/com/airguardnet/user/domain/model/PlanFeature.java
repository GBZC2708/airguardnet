package com.airguardnet.user.domain.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlanFeature {
    private Long id;
    private Long planId;
    private String featureKey;
    private boolean enabled;
}
