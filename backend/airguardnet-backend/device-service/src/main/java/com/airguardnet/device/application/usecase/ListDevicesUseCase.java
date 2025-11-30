package com.airguardnet.device.application.usecase;

import com.airguardnet.device.domain.model.Device;
import com.airguardnet.device.domain.repository.DeviceRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListDevicesUseCase {

    private final DeviceRepositoryPort deviceRepositoryPort;

    public List<Device> execute() {
        return deviceRepositoryPort.findAll();
    }
}
