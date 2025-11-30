package com.airguardnet.device.infrastructure.persistence;

import com.airguardnet.device.domain.model.Reading;
import com.airguardnet.device.domain.repository.ReadingRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ReadingRepositoryAdapter implements ReadingRepositoryPort {
    private final ReadingJpaRepository readingJpaRepository;

    @Override
    public Reading save(Reading reading) {
        return readingJpaRepository.save(ReadingEntity.fromDomain(reading)).toDomain();
    }

    @Override
    public List<Reading> findByDeviceId(Long deviceId, Integer limit) {
        int size = limit != null && limit > 0 ? limit : 50;
        return readingJpaRepository.findByDeviceIdOrderByRecordedAtDesc(deviceId, PageRequest.of(0, size))
                .stream()
                .map(ReadingEntity::toDomain)
                .toList();
    }
}
