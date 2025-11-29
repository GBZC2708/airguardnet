package com.airguardnet.device.application.usecase;

import com.airguardnet.device.domain.model.SensorConfig;
import com.airguardnet.device.domain.repository.SensorConfigRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListSensorConfigsUseCase {

    private final SensorConfigRepositoryPort sensorConfigRepositoryPort;

    public List<SensorConfig> execute() {
        return sensorConfigRepositoryPort.findAll();
    }
}
