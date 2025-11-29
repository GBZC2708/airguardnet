package com.airguardnet.device.domain.repository;

import com.airguardnet.device.domain.model.Alert;

public interface AlertRepositoryPort {
    Alert save(Alert alert);
}
