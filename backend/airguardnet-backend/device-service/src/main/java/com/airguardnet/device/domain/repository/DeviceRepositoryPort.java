package com.airguardnet.device.domain.repository;

import com.airguardnet.device.domain.model.Device;

import java.util.List;
import java.util.Optional;

public interface DeviceRepositoryPort {
    Optional<Device> findByDeviceUid(String uid);
    Device save(Device device);
    Optional<Device> findById(Long id);
    List<Device> findAll();
}
