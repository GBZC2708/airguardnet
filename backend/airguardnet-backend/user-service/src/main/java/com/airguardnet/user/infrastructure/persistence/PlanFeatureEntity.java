package com.airguardnet.user.infrastructure.persistence;

import com.airguardnet.user.domain.model.PlanFeature;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "plan_features")
@Data
public class PlanFeatureEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long planId;
    private String featureKey;
    private boolean enabled;

    public PlanFeature toDomain() {
        return PlanFeature.builder()
                .id(id)
                .planId(planId)
                .featureKey(featureKey)
                .enabled(enabled)
                .build();
    }
}
