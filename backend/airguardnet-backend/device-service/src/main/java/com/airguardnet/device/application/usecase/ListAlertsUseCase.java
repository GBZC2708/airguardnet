package com.airguardnet.device.application.usecase;

import com.airguardnet.device.domain.model.Alert;
import com.airguardnet.device.domain.repository.AlertRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListAlertsUseCase {

    private final AlertRepositoryPort alertRepositoryPort;

    public List<Alert> execute(Integer limit, String status, String severity) {
        return alertRepositoryPort.search(limit, status, severity);
    }
}
