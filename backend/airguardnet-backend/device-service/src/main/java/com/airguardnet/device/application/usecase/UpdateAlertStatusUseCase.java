package com.airguardnet.device.application.usecase;

import com.airguardnet.device.domain.model.Alert;
import com.airguardnet.device.domain.repository.AlertRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UpdateAlertStatusUseCase {

    private final AlertRepositoryPort alertRepositoryPort;

    public Optional<Alert> execute(Long id, String status) {
        return alertRepositoryPort.findById(id).map(alert -> {
            alert.setStatus(status);
            return alertRepositoryPort.save(alert);
        });
    }
}
