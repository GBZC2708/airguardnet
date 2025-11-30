package com.airguardnet.device.application.usecase;

import com.airguardnet.device.domain.model.Reading;
import com.airguardnet.device.domain.repository.ReadingRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListDeviceReadingsUseCase {

    private final ReadingRepositoryPort readingRepositoryPort;

    public List<Reading> execute(Long deviceId, Integer limit) {
        return readingRepositoryPort.findByDeviceId(deviceId, limit);
    }
}
