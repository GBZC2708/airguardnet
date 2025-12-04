package com.airguardnet.device.domain.repository;

import com.airguardnet.device.domain.model.FirmwareVersion;

public interface FirmwareVersionRepositoryPort {
    FirmwareVersion save(FirmwareVersion version);
}
