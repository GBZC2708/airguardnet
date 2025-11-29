package com.airguardnet.device.infrastructure.persistence;

import com.airguardnet.device.domain.model.Device;
import com.airguardnet.device.domain.repository.DeviceRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DeviceRepositoryAdapter implements DeviceRepositoryPort {
    private final DeviceJpaRepository deviceJpaRepository;

    @Override
    public Optional<Device> findByDeviceUid(String uid) {
        return deviceJpaRepository.findByDeviceUid(uid).map(DeviceEntity::toDomain);
    }

    @Override
    public Device save(Device device) {
        return deviceJpaRepository.save(DeviceEntity.fromDomain(device)).toDomain();
    }

    @Override
    public Optional<Device> findById(Long id) {
        return deviceJpaRepository.findById(id).map(DeviceEntity::toDomain);
    }

    @Override
    public List<Device> findAll() {
        return deviceJpaRepository.findAll().stream().map(DeviceEntity::toDomain).toList();
    }
}
