package com.airguardnet.user.infrastructure.persistence;

import com.airguardnet.user.domain.model.Plan;
import com.airguardnet.user.domain.repository.PlanRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PlanRepositoryAdapter implements PlanRepositoryPort {
    private final PlanJpaRepository planJpaRepository;

    @Override
    public List<Plan> findAll() {
        return planJpaRepository.findAll().stream().map(PlanEntity::toDomain).toList();
    }
}
