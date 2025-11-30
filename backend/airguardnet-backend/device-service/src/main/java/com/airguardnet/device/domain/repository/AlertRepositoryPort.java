package com.airguardnet.device.domain.repository;

import com.airguardnet.device.domain.model.Alert;

public interface AlertRepositoryPort {
    Alert save(Alert alert);
    java.util.Optional<Alert> findById(Long id);
    java.util.List<Alert> findByDeviceId(Long deviceId);
    java.util.List<Alert> search(Integer limit, String status, String severity);
}
