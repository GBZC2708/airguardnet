package com.airguardnet.device.application.usecase;

import com.airguardnet.device.domain.model.Device;
import com.airguardnet.device.domain.repository.DeviceRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GetDeviceUseCase {

    private final DeviceRepositoryPort deviceRepositoryPort;

    public Optional<Device> execute(Long id) {
        return deviceRepositoryPort.findById(id);
    }
}
