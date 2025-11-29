package com.airguardnet.user.infrastructure.persistence;

import com.airguardnet.user.domain.model.SystemLog;
import com.airguardnet.user.domain.repository.SystemLogRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SystemLogRepositoryAdapter implements SystemLogRepositoryPort {
    private final SystemLogJpaRepository systemLogJpaRepository;

    @Override
    public SystemLog save(SystemLog log) {
        return systemLogJpaRepository.save(SystemLogEntity.fromDomain(log)).toDomain();
    }

    @Override
    public List<SystemLog> findAllOrderedByCreatedAtDesc() {
        return systemLogJpaRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(SystemLogEntity::toDomain)
                .collect(Collectors.toList());
    }
}
