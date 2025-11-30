package com.airguardnet.device.infrastructure.persistence;

import com.airguardnet.device.domain.model.Alert;
import com.airguardnet.device.domain.repository.AlertRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AlertRepositoryAdapter implements AlertRepositoryPort {
    private final AlertJpaRepository alertJpaRepository;

    @Override
    public Alert save(Alert alert) {
        return alertJpaRepository.save(AlertEntity.fromDomain(alert)).toDomain();
    }

    @Override
    public Optional<Alert> findById(Long id) {
        return alertJpaRepository.findById(id).map(AlertEntity::toDomain);
    }

    @Override
    public List<Alert> findByDeviceId(Long deviceId) {
        return alertJpaRepository.findByDeviceIdOrderByCreatedAtDesc(deviceId)
                .stream()
                .map(AlertEntity::toDomain)
                .toList();
    }

    @Override
    public List<Alert> search(Integer limit, String status, String severity) {
        int size = limit != null && limit > 0 ? limit : 50;
        if (status != null && severity != null) {
            return alertJpaRepository.findByStatusAndSeverityOrderByCreatedAtDesc(status, severity, PageRequest.of(0, size))
                    .stream()
                    .map(AlertEntity::toDomain)
                    .toList();
        }
        if (status != null) {
            return alertJpaRepository.findByStatusOrderByCreatedAtDesc(status, PageRequest.of(0, size))
                    .stream()
                    .map(AlertEntity::toDomain)
                    .toList();
        }
        if (severity != null) {
            return alertJpaRepository.findBySeverityOrderByCreatedAtDesc(severity, PageRequest.of(0, size))
                    .stream()
                    .map(AlertEntity::toDomain)
                    .toList();
        }
        return alertJpaRepository.findAllByOrderByCreatedAtDesc(PageRequest.of(0, size))
                .stream()
                .map(AlertEntity::toDomain)
                .toList();
    }
}
