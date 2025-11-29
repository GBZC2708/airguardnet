package com.airguardnet.user.infrastructure.persistence;

import com.airguardnet.user.domain.model.SystemLog;
import com.airguardnet.user.domain.repository.SystemLogRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SystemLogRepositoryAdapter implements SystemLogRepositoryPort {
    private final SystemLogJpaRepository systemLogJpaRepository;

    @Override
    public SystemLog save(SystemLog log) {
        return systemLogJpaRepository.save(SystemLogEntity.fromDomain(log)).toDomain();
    }
}
