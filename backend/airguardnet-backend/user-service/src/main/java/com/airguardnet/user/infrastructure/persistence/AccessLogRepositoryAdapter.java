package com.airguardnet.user.infrastructure.persistence;

import com.airguardnet.user.domain.model.AccessLog;
import com.airguardnet.user.domain.repository.AccessLogRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccessLogRepositoryAdapter implements AccessLogRepositoryPort {
    private final AccessLogJpaRepository accessLogJpaRepository;

    @Override
    public AccessLog save(AccessLog log) {
        return accessLogJpaRepository.save(AccessLogEntity.fromDomain(log)).toDomain();
    }
}
