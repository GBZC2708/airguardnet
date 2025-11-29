package com.airguardnet.device.infrastructure.persistence;

import com.airguardnet.device.domain.model.Alert;
import com.airguardnet.device.domain.repository.AlertRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AlertRepositoryAdapter implements AlertRepositoryPort {
    private final AlertJpaRepository alertJpaRepository;

    @Override
    public Alert save(Alert alert) {
        return alertJpaRepository.save(AlertEntity.fromDomain(alert)).toDomain();
    }
}
