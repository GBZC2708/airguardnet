package com.airguardnet.user.domain.repository;

import com.airguardnet.user.domain.model.Plan;

import java.util.List;

public interface PlanRepositoryPort {
    List<Plan> findAll();
}
