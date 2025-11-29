package com.airguardnet.device.domain.repository;

import com.airguardnet.device.domain.model.Reading;

public interface ReadingRepositoryPort {
    Reading save(Reading reading);
}
