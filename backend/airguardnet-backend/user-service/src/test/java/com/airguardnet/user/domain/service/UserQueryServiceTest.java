// Cobertura matriz Nro 55
package com.airguardnet.user.domain.service;

import com.airguardnet.user.domain.model.Plan;
import com.airguardnet.user.domain.model.User;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class UserQueryServiceTest {

    private final UserQueryService userQueryService = new UserQueryService();

    @Test
    // Nro 55: Filtrar usuarios por plan
    void findByPlanName_filtersByFreePlan() {
        Plan free = Plan.builder().id(1L).name("Free").build();
        Plan enterprise = Plan.builder().id(2L).name("Enterprise").build();
        List<User> users = List.of(
                User.builder().plan(free).build(),
                User.builder().plan(enterprise).build(),
                User.builder().plan(free).build()
        );

        List<User> freeUsers = userQueryService.findByPlanName(users, "Free");

        assertTrue(freeUsers.stream().allMatch(u -> "Free".equals(u.getPlan().getName())));
    }
}
