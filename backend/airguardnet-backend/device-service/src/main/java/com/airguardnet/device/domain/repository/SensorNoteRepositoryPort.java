package com.airguardnet.device.domain.repository;

import com.airguardnet.device.domain.model.SensorNote;

public interface SensorNoteRepositoryPort {
    SensorNote save(SensorNote note);
}
