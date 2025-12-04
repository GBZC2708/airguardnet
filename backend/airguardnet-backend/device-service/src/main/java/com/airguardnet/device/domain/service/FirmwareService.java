package com.airguardnet.device.domain.service;

import com.airguardnet.device.domain.model.FirmwareVersion;
import com.airguardnet.device.domain.repository.FirmwareVersionRepositoryPort;

public class FirmwareService {

    private final FirmwareVersionRepositoryPort firmwareVersionRepositoryPort;

    public FirmwareService(FirmwareVersionRepositoryPort firmwareVersionRepositoryPort) {
        this.firmwareVersionRepositoryPort = firmwareVersionRepositoryPort;
    }

    public FirmwareVersion create(String code, String desc, boolean recommended) {
        FirmwareVersion version = FirmwareVersion.builder()
                .versionCode(code)
                .description(desc)
                .recommended(recommended)
                .build();
        return firmwareVersionRepositoryPort.save(version);
    }

    public FirmwareVersion updateDescription(FirmwareVersion version, String desc) {
        version.setDescription(desc);
        return firmwareVersionRepositoryPort.save(version);
    }
}
