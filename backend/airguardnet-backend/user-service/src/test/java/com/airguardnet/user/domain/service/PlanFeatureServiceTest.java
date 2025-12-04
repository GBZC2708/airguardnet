package com.airguardnet.user.domain.service;

import com.airguardnet.user.domain.model.Plan;
import com.airguardnet.user.domain.model.PlanFeature;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PlanFeatureServiceTest {

    @Test
    // Nro 39: Validar que plan Free no tenga REPORTS_EXPORT
    void hasFeature_freePlanDoesNotHaveReportsExport() {
        Plan free = Plan.builder().id(1L).name("Free").build();
        List<PlanFeature> features = Arrays.asList(
                PlanFeature.builder().planId(1L).featureKey("ALERT_PUSH").enabled(false).build(),
                PlanFeature.builder().planId(1L).featureKey("REPORTS_EXPORT").enabled(false).build()
        );

        PlanFeatureService service = new PlanFeatureService();
        assertFalse(service.hasFeature(free, features, "REPORTS_EXPORT"));
    }

    @Test
    // Nro 40: Validar que plan Enterprise tenga ALERT_PUSH
    void hasFeature_enterpriseHasAlertPush() {
        Plan enterprise = Plan.builder().id(2L).name("Enterprise").build();
        List<PlanFeature> features = Arrays.asList(
                PlanFeature.builder().planId(2L).featureKey("ALERT_PUSH").enabled(true).build(),
                PlanFeature.builder().planId(2L).featureKey("REPORTS_EXPORT").enabled(true).build()
        );

        PlanFeatureService service = new PlanFeatureService();
        assertTrue(service.hasFeature(enterprise, features, "ALERT_PUSH"));
    }
}
