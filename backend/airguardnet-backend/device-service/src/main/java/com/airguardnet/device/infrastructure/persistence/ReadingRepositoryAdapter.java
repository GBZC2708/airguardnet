package com.airguardnet.device.infrastructure.persistence;

import com.airguardnet.device.domain.model.Reading;
import com.airguardnet.device.domain.repository.ReadingRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReadingRepositoryAdapter implements ReadingRepositoryPort {
    private final ReadingJpaRepository readingJpaRepository;

    @Override
    public Reading save(Reading reading) {
        return readingJpaRepository.save(ReadingEntity.fromDomain(reading)).toDomain();
    }
}
