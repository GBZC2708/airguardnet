package com.airguardnet.device.application.usecase;

import com.airguardnet.device.domain.model.Alert;
import com.airguardnet.device.domain.repository.AlertRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListDeviceAlertsUseCase {

    private final AlertRepositoryPort alertRepositoryPort;

    public List<Alert> execute(Long deviceId) {
        return alertRepositoryPort.findByDeviceId(deviceId);
    }
}
