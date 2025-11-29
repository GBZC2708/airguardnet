package com.airguardnet.user.application.usecase;

import com.airguardnet.user.domain.model.Plan;
import com.airguardnet.user.domain.repository.PlanRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListPlansUseCase {
    private final PlanRepositoryPort planRepositoryPort;

    public List<Plan> execute() {
        return planRepositoryPort.findAll();
    }
}
